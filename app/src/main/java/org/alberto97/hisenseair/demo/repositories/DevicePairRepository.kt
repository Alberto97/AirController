package org.alberto97.hisenseair.demo.repositories

import org.alberto97.hisenseair.ayla.models.Device
import org.alberto97.hisenseair.ayla.models.Status
import org.alberto97.hisenseair.ayla.models.WifiScanResults
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.repositories.IDevicePairRepository

class DevicePairRepository : IDevicePairRepository {
    override suspend fun getStatus(): ResultWrapper<Status> {
        TODO("Not yet implemented")
    }

    override suspend fun getNetworks(): ResultWrapper<List<WifiScanResults.WifiScanResult>> {
        TODO("Not yet implemented")
    }

    override suspend fun connect(
        ssid: String,
        password: String?,
        setupToken: String
    ): ResultWrapper<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun pair(dsn: String, setupToken: String): ResultWrapper<Device> {
        TODO("Not yet implemented")
    }
}