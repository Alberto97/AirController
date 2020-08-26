package org.alberto97.hisenseair.repositories

import android.util.Log
import com.google.gson.Gson
import org.alberto97.hisenseair.AylaService
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.features.TemperatureExtensions.isCelsius
import org.alberto97.hisenseair.features.TemperatureExtensions.toC
import org.alberto97.hisenseair.features.TemperatureExtensions.toF
import org.alberto97.hisenseair.models.*

interface IDeviceRepository {
    suspend fun getDevices(): List<Device>
    suspend fun getDevice(dsn: String): Device
    suspend fun getDeviceState(dsn: String): DeviceState
    suspend fun setDeviceName(name: String, dsn: String)
    suspend fun setProperty(property: String, value: Int, dsn: String)
    suspend fun getTempUnit(dsn: String): TempType
    suspend fun setTempUnit(dsn: String, unit: TempType)
    suspend fun setTemp(value: Int, dsn: String)
}

class DeviceState {
    var productName: String = "Device"
    var backlight: Boolean = false
    var workMode: WorkMode = WorkMode.Auto
    var horizontalAirFlow: Boolean = false
    var verticalAirFlow: Boolean = false
    var quiet: Boolean = false
    var eco: Boolean = false
    var boost: Boolean = false
    var sleepMode: Int = 0
    var fanSpeed: FanSpeed = FanSpeed.Auto
    var temp: Int = 0
    var roomTemp: Int = 0
    var tempUnit: TempType = TempType.Fahrenheit
    var on: Boolean = false
    var maxTemp = 90
    var minTemp = 61
}

class DeviceRepository(private val service: AylaService, private val prefs: IDevicePreferencesRepository) : IDeviceRepository {

    override suspend fun getDevices(): List<Device> {
        val devicesWrapperList = service.getDevices()
        return devicesWrapperList.map { it.device }
    }

    override suspend fun getDevice(dsn: String): Device {
        val resp = service.getDevice(dsn)
        return resp.device
    }

    override suspend fun getDeviceState(dsn: String): DeviceState {
        val deviceState = DeviceState()

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

    override suspend fun setDeviceName(name: String, dsn: String) {
        val x = ProductName(name)
        val c = ProductNameWrapper(x)
        service.putDevice(dsn, c)
    }

    override suspend fun setProperty(property: String, value: Int, dsn: String) {
        val datapoint = Datapoint(value)
        val data = DatapointWrapper(datapoint)

        val out = service.setDeviceProperty(dsn, property, data)
        Log.d("hisense", Gson().toJson(out))
    }

    private suspend fun getProperty(property: String, dsn: String): Property {
        val wrappedValue = service.getDeviceProperty(dsn, property)
        return wrappedValue.property
    }

    private suspend fun getBooleanProperty(property: String, dsn: String): Boolean {
        val value = getProperty(property, dsn)
        return mapPropertyValue(value) as Boolean
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

    override suspend fun setTemp(value: Int, dsn: String) {
        val unit = prefs.getTempUnit(dsn)
        val temp = if (unit.isCelsius()) value.toF() else value
        setProperty(TEMP_PROP, temp, dsn)
    }
}