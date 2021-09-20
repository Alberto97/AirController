package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.models.DevicePairStatus
import org.alberto97.hisenseair.models.DevicePairWifiScanResult
import org.alberto97.hisenseair.models.ResultWrapper

interface IDevicePairRepository {
    suspend fun getStatus(): ResultWrapper<DevicePairStatus>
    suspend fun getNetworks(): ResultWrapper<List<DevicePairWifiScanResult>>
    suspend fun connect(ssid: String, password: String?, setupToken: String) : ResultWrapper<Unit>
    suspend fun pair(dsn: String, setupToken: String): ResultWrapper<DevicePairStatus>
}