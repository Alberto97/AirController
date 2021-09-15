package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.models.ResultWrapper

interface IDeviceRepository {
    suspend fun getDevices(): ResultWrapper<List<AppDevice>>
    suspend fun getDevice(dsn: String): ResultWrapper<AppDevice>
    suspend fun setDeviceName(name: String, dsn: String): ResultWrapper<Unit>
    suspend fun deleteDevice(dsn: String): ResultWrapper<Unit>
    suspend fun getTempUnit(dsn: String): ResultWrapper<TempType>
    suspend fun setTempUnit(dsn: String, value: TempType): ResultWrapper<Unit>
}