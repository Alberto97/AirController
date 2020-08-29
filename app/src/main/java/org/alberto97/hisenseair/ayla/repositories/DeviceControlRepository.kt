package org.alberto97.hisenseair.ayla.repositories

import org.alberto97.hisenseair.ayla.AylaPropertyToStateMap
import org.alberto97.hisenseair.ayla.features.*
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

class DeviceControlRepository(private val service: AylaService, private val prefs: IDevicePreferencesRepository) :
    IDeviceControlRepository {

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
            var value = mapPropertyValue(it)

            when (it.name) {
                WORK_MODE_PROP -> value = WorkMode.from(value as Int)
                FAN_SPEED_PROP -> value = FanSpeed.from(value as Int)
                TEMP_TYPE_PROP -> value = TempTypeMap.getValue(value as Boolean)
            }

            val prop = AylaPropertyToStateMap[it.name]
            if (prop != null) {
                prop.setter.call(deviceState, value)
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