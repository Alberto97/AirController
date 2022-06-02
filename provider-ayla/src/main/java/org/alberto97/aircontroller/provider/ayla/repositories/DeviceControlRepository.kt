package org.alberto97.aircontroller.provider.ayla.repositories

import android.util.Log
import org.alberto97.aircontroller.common.features.*
import org.alberto97.aircontroller.common.features.TemperatureExtensions.isCelsius
import org.alberto97.aircontroller.common.features.TemperatureExtensions.toC
import org.alberto97.aircontroller.common.features.TemperatureExtensions.toF
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.common.models.DefaultErrors
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.provider.ayla.internal.*
import org.alberto97.aircontroller.provider.ayla.internal.converters.IFanSpeedConverter
import org.alberto97.aircontroller.provider.ayla.internal.converters.IModeConverter
import org.alberto97.aircontroller.provider.ayla.internal.converters.ISleepModeConverter
import org.alberto97.aircontroller.provider.ayla.internal.converters.ITempUnitConverter
import org.alberto97.aircontroller.provider.ayla.internal.models.BooleanDatapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.BooleanProperty
import org.alberto97.aircontroller.provider.ayla.internal.models.IntDatapoint
import org.alberto97.aircontroller.provider.ayla.internal.models.IntProperty
import org.alberto97.aircontroller.provider.ayla.internal.repositories.IDeviceCacheRepository
import org.alberto97.aircontroller.provider.ayla.internal.repositories.IDevicePropertyRepository
import org.alberto97.aircontroller.provider.repositories.IDeviceControlRepository
import java.io.IOException

internal class DeviceControlRepository(
    private val propertyRepo: IDevicePropertyRepository,
    private val prefs: IDeviceCacheRepository,
    private val fanSpeedConverter: IFanSpeedConverter,
    private val modeConverter: IModeConverter,
    private val tempUnitConverter: ITempUnitConverter,
    private val sleepModeConverter: ISleepModeConverter
) : IDeviceControlRepository {
    companion object {
        private const val LOG_TAG = "HiDeviceControl"
    }

    override suspend fun getDeviceState(dsn: String): ResultWrapper<AppDeviceState> {
        val deviceState = AppDeviceState()

        val props = try {
            propertyRepo.getProperties(dsn)
        } catch (e: Exception) {
            if (e is IOException)
                return DefaultErrors.connectivityError()
            Log.e(LOG_TAG, e.stackTraceToString())
            return ResultWrapper.Error("Cannot retrieve device state")
        }

        // Set device name
        deviceState.productName = props.first().productName

        val intMap = mutableMapOf<String, Int?>()
        val boolMap = mutableMapOf<String, Boolean?>()
        props.forEach { property ->
            when (property) {
                is BooleanProperty -> boolMap[property.name] = property.value
                is IntProperty -> intMap[property.name] = property.value
                else -> {}
            }
        }

        val intWorkMode = intMap[WORK_MODE_PROP]!!
        val workMode = modeConverter.fromAyla(intWorkMode)

        val intFanSpeed = intMap[FAN_SPEED_PROP]!!
        val fanSpeed = fanSpeedConverter.fromAyla(intFanSpeed)

        val boolTempUnit = boolMap[TEMP_TYPE_PROP]!!
        val tempUnit = tempUnitConverter.fromAyla(boolTempUnit)

        val intSleepMode = intMap[SLEEP_MODE_PROP]!!
        val sleepMode = sleepModeConverter.fromAyla(intSleepMode)

        // Modes
        deviceState.supportedModes = getSupportedModes()
        deviceState.workMode = workMode
        deviceState.supportedSpeeds = getSupportedFanSpeeds()
        deviceState.fanSpeed = fanSpeed
        deviceState.supportedSleepModes = getSupportedSleepModes(deviceState.workMode)
        deviceState.sleepMode = sleepMode
        deviceState.tempUnit = tempUnit

        // Booleans
        deviceState.on = boolMap[POWER_PROP] ?: false
        deviceState.backlight = boolMap[BACKLIGHT_PROP] ?: false
        deviceState.eco = boolMap[ECO_PROP] ?: false
        deviceState.quiet = boolMap[QUIET_PROP] ?: false
        deviceState.boost = boolMap[BOOST_PROP] ?: false
        deviceState.horizontalAirFlow = boolMap[HORIZONTAL_AIR_FLOW_PROP] ?: false
        deviceState.verticalAirFlow = boolMap[VERTICAL_AIR_FLOW_PROP] ?: false

        // Temperatures
        deviceState.roomTemp = intMap[ROOM_TEMP_PROP] ?: 0
        deviceState.temp = intMap[TEMP_PROP] ?: 0
        deviceState.maxTemp = getMaxTemp(deviceState.tempUnit)
        deviceState.minTemp = getMinTemp(deviceState.tempUnit)

        prefs.setTempUnit(dsn, deviceState.tempUnit)
        if (deviceState.tempUnit.isCelsius()) {
            deviceState.temp = deviceState.temp.toC()
            deviceState.roomTemp = deviceState.roomTemp.toC()
        }

        return ResultWrapper.Success(deviceState)
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

    override suspend fun setAirFlowHorizontal(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return try {
            val datapoint = BooleanDatapoint(value)
            propertyRepo.setProperty(dsn, HORIZONTAL_AIR_FLOW_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update horizontal air flow")
        }
    }

    override suspend fun setAirFlowVertical(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return try {
            val datapoint = BooleanDatapoint(value)
            propertyRepo.setProperty(dsn, VERTICAL_AIR_FLOW_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update vertical air flow")
        }
    }

    override suspend fun setBacklight(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return try {
            val datapoint = BooleanDatapoint(value)
            propertyRepo.setProperty(dsn, BACKLIGHT_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update backlight")
        }
    }

    override suspend fun setBoost(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return try {
            val datapoint = BooleanDatapoint(value)
            propertyRepo.setProperty(dsn, BOOST_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update boost mode")
        }
    }

    override suspend fun setEco(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return try {
            val datapoint = BooleanDatapoint(value)
            propertyRepo.setProperty(dsn, ECO_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update eco mode")
        }
    }

    override suspend fun setFanSpeed(dsn: String, value: FanSpeed): ResultWrapper<Unit> {
        return try {
            val data = fanSpeedConverter.toAyla(value)
            val datapoint = IntDatapoint(data)
            propertyRepo.setProperty(dsn, FAN_SPEED_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update fan speed")
        }
    }

    override suspend fun setMode(dsn: String, value: WorkMode): ResultWrapper<Unit> {
        return try {
            val data = modeConverter.toAyla(value)
            val datapoint = IntDatapoint(data)
            propertyRepo.setProperty(dsn, WORK_MODE_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update work mode")
        }
    }

    override suspend fun setPower(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return try {
            val datapoint = BooleanDatapoint(value)
            propertyRepo.setProperty(dsn, POWER_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update power status")
        }
    }

    override suspend fun setQuiet(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return try {
            val datapoint = BooleanDatapoint(value)
            propertyRepo.setProperty(dsn, QUIET_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update quiet mode")
        }
    }

    override suspend fun setSleepMode(dsn: String, value: SleepMode): ResultWrapper<Unit> {
        return try {
            val data = sleepModeConverter.toAyla(value)
            val datapoint = IntDatapoint(data)
            propertyRepo.setProperty(dsn, SLEEP_MODE_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update sleep mode")
        }
    }

    override suspend fun setTemp(dsn: String, value: Int): ResultWrapper<Unit> {
        return try {
            val unit = prefs.getTempUnit(dsn)
            val temp = if (unit.isCelsius()) value.toF() else value
            val datapoint = IntDatapoint(temp)
            propertyRepo.setProperty(dsn, TEMP_PROP, datapoint)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            ResultWrapper.Error("Cannot update temperature")
        }
    }
}