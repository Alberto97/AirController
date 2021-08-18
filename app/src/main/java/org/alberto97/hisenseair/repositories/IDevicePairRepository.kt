package org.alberto97.hisenseair.repositories

import org.alberto97.hisenseair.ayla.models.Device
import org.alberto97.hisenseair.ayla.models.Status
import org.alberto97.hisenseair.ayla.models.WifiScanResults
import org.alberto97.hisenseair.models.ResultWrapper

interface IDevicePairRepository {
    suspend fun getStatus(): ResultWrapper<Status>
    suspend fun getNetworks(): ResultWrapper<List<WifiScanResults.WifiScanResult>>
    suspend fun connect(ssid: String, password: String?, setupToken: String) : ResultWrapper<Unit>
    suspend fun pair(dsn: String, setupToken: String): ResultWrapper<Device>
}