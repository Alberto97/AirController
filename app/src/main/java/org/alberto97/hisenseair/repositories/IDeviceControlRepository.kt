package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.models.AppDeviceState

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
    suspend fun setSleepMode(dsn: String, value: SleepMode)
    suspend fun setTemp(dsn: String, value: Int)
    suspend fun getTempUnit(dsn: String): TempType
    suspend fun setTempUnit(dsn: String, value: TempType)
}

