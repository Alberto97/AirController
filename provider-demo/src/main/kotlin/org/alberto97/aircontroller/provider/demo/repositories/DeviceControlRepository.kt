package org.alberto97.aircontroller.provider.demo.repositories

import org.alberto97.aircontroller.provider.demo.internal.DemoStateHolder
import org.alberto97.aircontroller.common.features.FanSpeed
import org.alberto97.aircontroller.common.features.SleepMode
import org.alberto97.aircontroller.common.features.WorkMode
import org.alberto97.aircontroller.common.models.AppDeviceState
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.provider.repositories.IDeviceControlRepository

internal class DeviceControlRepository(val state: DemoStateHolder): IDeviceControlRepository {
    override suspend fun getDeviceState(dsn: String): ResultWrapper<AppDeviceState> {
        val deviceState = state.states.getValue(dsn)
        val device = state.devices.getValue(dsn)
        val state = deviceState.copy(productName = device.name)
        return ResultWrapper.Success(state)
    }

    override suspend fun setAirFlowHorizontal(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(horizontalAirFlow = value)
        }
    }

    override suspend fun setAirFlowVertical(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(verticalAirFlow = value)
        }
    }

    override suspend fun setBacklight(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(backlight = value)
        }
    }

    override suspend fun setBoost(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(boost = value)
        }
    }

    override suspend fun setEco(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(eco = value)
        }
    }

    override suspend fun setFanSpeed(dsn: String, value: FanSpeed): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(fanSpeed = value)
        }
    }

    override suspend fun setMode(dsn: String, value: WorkMode): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(workMode = value)
        }
    }

    override suspend fun setPower(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(on = value)
        }
    }

    override suspend fun setQuiet(dsn: String, value: Boolean): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(quiet = value)
        }
    }

    override suspend fun setSleepMode(dsn: String, value: SleepMode): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(sleepMode = value)
        }
    }

    override suspend fun setTemp(dsn: String, value: Int): ResultWrapper<Unit> {
        return setDeviceState(dsn) { deviceState ->
            deviceState.copy(temp = value)
        }
    }

    private fun setDeviceState(dsn: String, callback: (value: AppDeviceState) -> AppDeviceState): ResultWrapper.Success<Unit> {
        val deviceState = state.states.getValue(dsn)
        state.states[dsn] = callback(deviceState)
        return ResultWrapper.Success(Unit)
    }
}
