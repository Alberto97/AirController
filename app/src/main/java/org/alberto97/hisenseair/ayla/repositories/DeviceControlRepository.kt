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
import org.koin.core.KoinComponent
import org.koin.core.inject

class DeviceControlRepository(
    private val service: AylaService,
    private val prefs: IDeviceCacheRepository) : IDeviceControlRepository, KoinComponent {

    private val boolConverter: IBooleanConverter by inject()
    private val intConverter: IIntConverter by inject()
    private val stringConverter: IStringConverter by inject()
    private val fanSpeedConverter: IFanSpeedConverter by inject()
    private val modeConverter: IModeConverter by inject()
    private val tempUnitConverter: ITempUnitConverter by inject()

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
                else -> mapByType(it)
            }

            val prop = AylaPropertyToStateMap[it.name]

            @Suppress("IfThenToSafeAccess")
            if (prop != null)
                prop.setter.call(deviceState, value)
        }

        if (deviceState.tempUnit.isCelsius()) {
            deviceState.temp = deviceState.temp.toC()
            deviceState.roomTemp = deviceState.roomTemp.toC()
            deviceState.maxTemp = 32
            deviceState.minTemp = 16
        } else {
            deviceState.maxTemp = 90
            deviceState.minTemp = 61
        }
        prefs.setTempUnit(dsn, deviceState.tempUnit)

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

    override suspend fun setSleepMode(dsn: String, value: Int) {
        val datapoint = intConverter.map(value)
        setProperty(dsn, SLEEP_MODE_PROP, datapoint)
    }

    override suspend fun setTemp(value: Int, dsn: String) {
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