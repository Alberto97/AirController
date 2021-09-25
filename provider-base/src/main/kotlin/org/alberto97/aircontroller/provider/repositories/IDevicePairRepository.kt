package org.alberto97.aircontroller.provider.repositories

import org.alberto97.aircontroller.common.models.DevicePairStatus
import org.alberto97.aircontroller.common.models.DevicePairWifiScanResult
import org.alberto97.aircontroller.common.models.ResultWrapper

interface IDevicePairRepository {
    suspend fun getStatus(): ResultWrapper<DevicePairStatus>
    suspend fun getNetworks(): ResultWrapper<List<DevicePairWifiScanResult>>
    suspend fun connect(ssid: String, password: String?, setupToken: String) : ResultWrapper<Unit>
    suspend fun pair(dsn: String, setupToken: String): ResultWrapper<DevicePairStatus>
}