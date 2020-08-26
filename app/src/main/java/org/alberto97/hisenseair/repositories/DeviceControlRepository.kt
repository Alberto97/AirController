package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.AylaService
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.features.TemperatureExtensions.isCelsius
import org.alberto97.hisenseair.features.TemperatureExtensions.toC
import org.alberto97.hisenseair.features.TemperatureExtensions.toF
import org.alberto97.hisenseair.models.AppDeviceState
import org.alberto97.hisenseair.models.Datapoint
import org.alberto97.hisenseair.models.DatapointWrapper
import org.alberto97.hisenseair.models.Property

interface IDeviceControlRepository {
    suspend fun getDeviceState(dsn: String): AppDeviceState
    suspend fun setAirFlowHorizontal(dsn: String, value: Boolean)
    suspend fun setAirFlowVertical(dsn: String, value: Boolean)
    suspend fun setBacklight(dsn: String, value: Boolean)
    suspend fun setBoost(dsn: String, value: Boolean)
    suspend fun setEco(dsn: String, value: Boolean)
    suspend fun setFanSpeed(dsn: String, value: FanSpeed)
    suspend fun setMode(dsn: String, value: WorkMode)
    suspend fun setPower(dsn: String, value: Boolean)
    suspend fun setQuiet(dsn: String, value: Boolean)
    suspend fun setSleepMode(dsn: String, value: Int)
    suspend fun setTemp(value: Int, dsn: String)
    suspend fun getTempUnit(dsn: String): TempType
    suspend fun setTempUnit(dsn: String, unit: TempType)
}

class DeviceControlRepository(private val service: AylaService, private val prefs: IDevicePreferencesRepository) : IDeviceControlRepository {

    private suspend fun getProperty(property: String, dsn: String): Property {
        val wrappedValue = service.getDeviceProperty(dsn, property)
        return wrappedValue.property
    }

    private suspend fun setProperty(property: String, value: Int, dsn: String) {
        val datapoint = Datapoint(value)
        val data = DatapointWrapper(datapoint)
        service.setDeviceProperty(dsn, property, data)
    }

    private suspend fun getBooleanProperty(property: String, dsn: String): Boolean {
        val value = getProperty(property, dsn)
        return mapPropertyValue(value) as Boolean
    }

    private suspend fun setBooleanProperty(property: String, value: Boolean, dsn: String) {
        val data = if (value) 1 else 0
        setProperty(property, data, dsn)
    }

    private fun mapPropertyValue(prop: Property) : Any? {
        if (prop.baseType == "string")
            return prop.value

        prop.value ?: return null

        val double = prop.value as Double
        return when (prop.baseType) {
            "boolean" -> double > 0
            "integer", "decimal" -> double.toInt()
            else -> throw Exception("Unknown base_type for ${prop.name}")
        }
    }

    override suspend fun getDeviceState(dsn: String): AppDeviceState {
        val deviceState = AppDeviceState()

        val wrappedProps = service.getDeviceProperties(dsn)
        val props = wrappedProps.map { it.property }

        // Set device name
        deviceState.productName = props.first().productName

        // Update device state
        props.forEach {
            val value = mapPropertyValue(it)
            with(deviceState) {
                when (it.name) {
                    BACKLIGHT_PROP -> backlight = value as Boolean
                    WORK_MODE_PROP -> workMode = WorkMode.from(value as Int)!!
                    HORIZONTAL_AIR_FLOW_PROP -> horizontalAirFlow = value as Boolean
                    VERTICAL_AIR_FLOW_PROP -> verticalAirFlow = value as Boolean
                    QUIET_PROP -> quiet = value as Boolean
                    ECO_PROP -> eco = value as Boolean
                    BOOST_PROP -> boost = value as Boolean
                    SLEEP_MODE_PROP -> sleepMode = value as Int
                    FAN_SPEED_PROP -> fanSpeed = FanSpeed.from(value as Int)!!
                    TEMP_PROP -> temp = value as Int
                    ROOM_TEMP_PROP -> roomTemp = value as Int
                    TEMP_TYPE_PROP -> tempUnit = TempTypeMap.getValue(value as Boolean)
                    POWER_PROP -> on = value as Boolean
                }
            }
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

    override suspend fun setAirFlowHorizontal(dsn: String, value: Boolean) {
        setBooleanProperty(HORIZONTAL_AIR_FLOW_PROP, value, dsn)
    }

    override suspend fun setAirFlowVertical(dsn: String, value: Boolean) {
        setBooleanProperty(VERTICAL_AIR_FLOW_PROP, value, dsn)
    }

    override suspend fun setBacklight(dsn: String, value: Boolean) {
        setBooleanProperty(BACKLIGHT_PROP, value, dsn)
    }

    override suspend fun setBoost(dsn: String, value: Boolean) {
        setBooleanProperty(BOOST_PROP, value, dsn)
    }

    override suspend fun setEco(dsn: String, value: Boolean) {
        setBooleanProperty(ECO_PROP, value, dsn)
    }

    override suspend fun setFanSpeed(dsn: String, value: FanSpeed) {
        setProperty(FAN_SPEED_PROP, value.value, dsn)
    }

    override suspend fun setMode(dsn: String, value: WorkMode) {
        setProperty(WORK_MODE_PROP, value.value, dsn)
    }

    override suspend fun setPower(dsn: String, value: Boolean) {
        setBooleanProperty(POWER_PROP, value, dsn)
    }

    override suspend fun setQuiet(dsn: String, value: Boolean) {
        setBooleanProperty(QUIET_PROP, value, dsn)
    }

    override suspend fun setSleepMode(dsn: String, value: Int) {
        setProperty(SLEEP_MODE_PROP, value, dsn)
    }

    override suspend fun setTemp(value: Int, dsn: String) {
        val unit = prefs.getTempUnit(dsn)
        val temp = if (unit.isCelsius()) value.toF() else value
        setProperty(TEMP_PROP, temp, dsn)
    }

    override suspend fun getTempUnit(dsn: String): TempType {
        val value = getBooleanProperty(TEMP_TYPE_PROP, dsn)
        val unit = TempTypeMap.getValue(value)
        prefs.setTempUnit(dsn, unit)
        return unit
    }

    override suspend fun setTempUnit(dsn: String, unit: TempType) {
        prefs.setTempUnit(dsn, unit)
        setProperty(TEMP_TYPE_PROP, unit.value, dsn)
    }
}