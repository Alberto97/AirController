package org.alberto97.hisenseair.demo.repositories

import kotlinx.coroutines.delay
import org.alberto97.hisenseair.demo.internal.DemoStateHolder
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.features.TemperatureExtensions.isCelsius
import org.alberto97.hisenseair.features.TemperatureExtensions.toC
import org.alberto97.hisenseair.features.TemperatureExtensions.toF
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.repositories.IDeviceRepository

internal class DeviceRepository(val state: DemoStateHolder) : IDeviceRepository {
    override suspend fun getDevices(): ResultWrapper<List<AppDevice>> {
        delay(200L)
        return ResultWrapper.Success(state.devices.values.toMutableList())
    }

    override suspend fun getDevice(dsn: String): ResultWrapper<AppDevice> {
        delay(200L)
        val device = state.devices.getValue(dsn)
        return ResultWrapper.Success(device)
    }

    override suspend fun setDeviceName(name: String, dsn: String): ResultWrapper<Unit> {
        val device = state.devices.getValue(dsn)
        state.devices[dsn] = device.copy(name = name)
        return ResultWrapper.Success(Unit)
    }

    override suspend fun deleteDevice(dsn: String): ResultWrapper<Unit> {
        state.devices.remove(dsn)
        state.states.remove(dsn)
        return ResultWrapper.Success(Unit)
    }

    override suspend fun getTempUnit(dsn: String): ResultWrapper<TempType> {
        val state = state.states.getValue(dsn)
        return ResultWrapper.Success(state.tempUnit)
    }

    override suspend fun setTempUnit(dsn: String, value: TempType): ResultWrapper<Unit> {
        val deviceState = state.states.getValue(dsn)

        val temp = if (value.isCelsius()) deviceState.temp.toC() else deviceState.temp.toF()
        val roomTemp = if (value.isCelsius()) deviceState.roomTemp.toC() else deviceState.roomTemp.toF()
        val minTemp = if (value.isCelsius()) 16 else 61
        val maxTemp = if (value.isCelsius()) 32 else 90

        state.states[dsn] = deviceState.copy(
            tempUnit = value, temp = temp, roomTemp = roomTemp, minTemp = minTemp, maxTemp = maxTemp
        )

        return ResultWrapper.Success(Unit)
    }
}