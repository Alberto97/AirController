package org.alberto97.hisenseair.ayla.repositories

import org.alberto97.hisenseair.ayla.AylaPropertyToStateMap
import org.alberto97.hisenseair.ayla.AylaType
import org.alberto97.hisenseair.ayla.features.*
import org.alberto97.hisenseair.ayla.features.converters.*
import org.alberto97.hisenseair.ayla.models.Datapoint
import org.alberto97.hisenseair.ayla.models.DatapointWrapper
import org.alberto97.hisenseair.ayla.models.Property
import org.alberto97.hisenseair.ayla.network.api.AylaService
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.features.TemperatureExtensions.isCelsius
import org.alberto97.hisenseair.features.TemperatureExtensions.toC
import org.alberto97.hisenseair.features.TemperatureExtensions.toF
import org.alberto97.hisenseair.models.AppDeviceState
import org.alberto97.hisenseair.repositories.IDeviceControlRepository

class DeviceControlRepository(
    private val service: AylaService,
    private val prefs: IDeviceCacheRepository,
    private val boolConverter: IBooleanConverter,
    private val intConverter: IIntConverter,
    private val stringConverter: IStringConverter,
    private val fanSpeedConverter: IFanSpeedConverter,
    private val modeConverter: IModeConverter,
    private val tempUnitConverter: ITempUnitConverter,
    private val sleepModeConverter: ISleepModeConverter
) : IDeviceControlRepository {


    private suspend fun getProperty(property: String, dsn: String): Property {
        val wrappedValue = service.getDeviceProperty(dsn, property)
        return wrappedValue.property
    }

    private suspend fun setProperty(dsn: String, property: String, value: Datapoint) {
        val data = DatapointWrapper(value)
        service.setDeviceProperty(dsn, property, data)
    }

    override suspend fun getDeviceState(dsn: String): AppDeviceState {
        val deviceState = AppDeviceState()

        val wrappedProps = service.getDeviceProperties(dsn)
        val props = wrappedProps.map { it.property }

        // Set device name
        deviceState.productName = props.first().productName

        // Update device state
        props.forEach {
            val value = when (it.name) {
                WORK_MODE_PROP -> modeConverter.map(it)
                FAN_SPEED_PROP -> fanSpeedConverter.map(it)
                TEMP_TYPE_PROP -> tempUnitConverter.map(it)
                SLEEP_MODE_PROP -> sleepModeConverter.map(it)
                else -> mapByType(it)
            }

            val prop = AylaPropertyToStateMap[it.name]

            @Suppress("IfThenToSafeAccess")
            if (prop != null)
                prop.setter.call(deviceState, value)
        }

        deviceState.supportedModes = getSupportedModes()
        deviceState.supportedSpeeds = getSupportedFanSpeeds()
        deviceState.supportedSleepModes = getSupportedSleepModes(deviceState.workMode)
        deviceState.maxTemp = getMaxTemp(deviceState.tempUnit)
        deviceState.minTemp = getMinTemp(deviceState.tempUnit)

        prefs.setTempUnit(dsn, deviceState.tempUnit)
        if (deviceState.tempUnit.isCelsius()) {
            deviceState.temp = deviceState.temp.toC()
            deviceState.roomTemp = deviceState.roomTemp.toC()
        }

        return deviceState
    }

    private fun mapByType(prop: Property) : Any? {
        prop.value ?: return null

        return when (prop.baseType) {
            AylaType.BOOLEAN -> boolConverter.map(prop)
            AylaType.DECIMAL,
            AylaType.INTEGER -> intConverter.map(prop)
            AylaType.STRING -> stringConverter.map(prop)
            else -> throw Exception("Unknown base_type for ${prop.name}")
        }
    }

    private fun getSupportedModes(): List<WorkMode> {
        return listOf(
            WorkMode.Heating,
            WorkMode.Cooling,
            WorkMode.Dry,
            WorkMode.FanOnly,
            WorkMode.Auto
        )
    }

    private fun getSupportedFanSpeeds(): List<FanSpeed> {
        return listOf(
            FanSpeed.Lower,
            FanSpeed.Low,
            FanSpeed.Normal,
            FanSpeed.High,
            FanSpeed.Higher,
            FanSpeed.Auto,
        )
    }

    private fun getMaxTemp(unit: TempType): Int {
        return if (unit.isCelsius())
            32
        else
            90
    }

    private fun getMinTemp(unit: TempType): Int {
        return if (unit.isCelsius())
            16
        else
            61
    }

    private fun getSupportedSleepModes(mode: WorkMode): List<SleepModeData> {
        //val data = service.getDeviceProperty(dsn, WORK_MODE_PROP)
        //val mode = modeConverter.map(data.property)
        val heating = mode == WorkMode.Heating
        val off = SleepModeData(SleepMode.OFF, listOf(), listOf())

        // SLEEP 1: 2h -> +2/-2
        // SLEEP 2: 2h -> +2/-2, 6h -> +1/-1, 7h -> +1/-1
        // SLEEP 3 Cooling: 1h -> -1, 2h -> -2, 6h -> -2, 7h -> -1
        // SLEEP 3 Heating: 1h -> +2, 2h -> +2, 6h -> +2, 7h -> +2
        var sleep1 = SleepModeData(SleepMode.MODE1, listOf(2), listOf(-2))
        var sleep2 = SleepModeData(SleepMode.MODE2, listOf(2, 6, 7), listOf(-2, -1, -1))
        var sleep3 = SleepModeData(SleepMode.MODE3, listOf(1, 2, 6, 7), listOf(-1, -2, -2, -1))
        val sleep4 = SleepModeData(SleepMode.MODE4, listOf(), listOf())

        if (heating) {
            sleep1 = SleepModeData(SleepMode.MODE1, listOf(2), listOf(+2))
            sleep2 = SleepModeData(SleepMode.MODE2, listOf(2, 6, 7), listOf(+2, +1, +1))
            sleep3 = SleepModeData(SleepMode.MODE3, listOf(1, 2, 6, 7), listOf(+2, +2, +2, +2))
        }

        return listOf(off, sleep1, sleep2, sleep3, sleep4)
    }

    override suspend fun setAirFlowHorizontal(dsn: String, value: Boolean) {
        val datapoint = boolConverter.map(value)
        setProperty(dsn, HORIZONTAL_AIR_FLOW_PROP, datapoint)
    }

    override suspend fun setAirFlowVertical(dsn: String, value: Boolean) {
        val datapoint = boolConverter.map(value)
        setProperty(dsn, VERTICAL_AIR_FLOW_PROP, datapoint)
    }

    override suspend fun setBacklight(dsn: String, value: Boolean) {
        val datapoint = boolConverter.map(value)
        setProperty(dsn, BACKLIGHT_PROP, datapoint)
    }

    override suspend fun setBoost(dsn: String, value: Boolean) {
        val datapoint = boolConverter.map(value)
        setProperty(dsn, BOOST_PROP, datapoint)
    }

    override suspend fun setEco(dsn: String, value: Boolean) {
        val datapoint = boolConverter.map(value)
        setProperty(dsn, ECO_PROP, datapoint)
    }

    override suspend fun setFanSpeed(dsn: String, value: FanSpeed) {
        val data = fanSpeedConverter.map(value)
        setProperty(dsn, FAN_SPEED_PROP, data)
    }

    override suspend fun setMode(dsn: String, value: WorkMode) {
        val data = modeConverter.map(value)
        setProperty(dsn, WORK_MODE_PROP, data)
    }

    override suspend fun setPower(dsn: String, value: Boolean) {
        val datapoint = boolConverter.map(value)
        setProperty(dsn, POWER_PROP, datapoint)
    }

    override suspend fun setQuiet(dsn: String, value: Boolean) {
        val datapoint = boolConverter.map(value)
        setProperty(dsn, QUIET_PROP, datapoint)
    }

    override suspend fun setSleepMode(dsn: String, value: SleepMode) {
        val datapoint = sleepModeConverter.map(value)
        setProperty(dsn, SLEEP_MODE_PROP, datapoint)
    }

    override suspend fun setTemp(dsn: String, value: Int) {
        val unit = prefs.getTempUnit(dsn)
        val temp = if (unit.isCelsius()) value.toF() else value
        val datapoint = intConverter.map(temp)
        setProperty(dsn, TEMP_PROP, datapoint)
    }

    override suspend fun getTempUnit(dsn: String): TempType {
        val value = getProperty(TEMP_TYPE_PROP, dsn)
        val unit = tempUnitConverter.map(value)
        prefs.setTempUnit(dsn, unit)
        return unit
    }

    override suspend fun setTempUnit(dsn: String, value: TempType) {
        prefs.setTempUnit(dsn, value)
        val datapoint = tempUnitConverter.map(value)
        setProperty(dsn, TEMP_TYPE_PROP, datapoint)
    }
}