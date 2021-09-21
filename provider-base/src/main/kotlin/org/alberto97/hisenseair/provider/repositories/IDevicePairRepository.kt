package org.alberto97.hisenseair.provider.repositories

import org.alberto97.hisenseair.common.models.DevicePairStatus
import org.alberto97.hisenseair.common.models.DevicePairWifiScanResult
import org.alberto97.hisenseair.common.models.ResultWrapper

interface IDevicePairRepository {
    suspend fun getStatus(): ResultWrapper<DevicePairStatus>
    suspend fun getNetworks(): ResultWrapper<List<DevicePairWifiScanResult>>
    suspend fun connect(ssid: String, password: String?, setupToken: String) : ResultWrapper<Unit>
    suspend fun pair(dsn: String, setupToken: String): ResultWrapper<DevicePairStatus>
}