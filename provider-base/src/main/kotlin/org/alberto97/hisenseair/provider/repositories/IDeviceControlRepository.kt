package org.alberto97.hisenseair.provider.repositories

import org.alberto97.hisenseair.common.features.*
import org.alberto97.hisenseair.common.models.AppDeviceState
import org.alberto97.hisenseair.common.models.ResultWrapper

interface IDeviceControlRepository {
    suspend fun getDeviceState(dsn: String): ResultWrapper<AppDeviceState>
    suspend fun setAirFlowHorizontal(dsn: String, value: Boolean): ResultWrapper<Unit>
    suspend fun setAirFlowVertical(dsn: String, value: Boolean): ResultWrapper<Unit>
    suspend fun setBacklight(dsn: String, value: Boolean): ResultWrapper<Unit>
    suspend fun setBoost(dsn: String, value: Boolean): ResultWrapper<Unit>
    suspend fun setEco(dsn: String, value: Boolean): ResultWrapper<Unit>
    suspend fun setFanSpeed(dsn: String, value: FanSpeed): ResultWrapper<Unit>
    suspend fun setMode(dsn: String, value: WorkMode): ResultWrapper<Unit>
    suspend fun setPower(dsn: String, value: Boolean): ResultWrapper<Unit>
    suspend fun setQuiet(dsn: String, value: Boolean): ResultWrapper<Unit>
    suspend fun setSleepMode(dsn: String, value: SleepMode): ResultWrapper<Unit>
    suspend fun setTemp(dsn: String, value: Int): ResultWrapper<Unit>
}

