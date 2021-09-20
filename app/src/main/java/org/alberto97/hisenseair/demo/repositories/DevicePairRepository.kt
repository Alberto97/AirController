package org.alberto97.hisenseair.demo.repositories

import org.alberto97.hisenseair.models.DevicePairStatus
import org.alberto97.hisenseair.models.DevicePairWifiScanResult
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.repositories.IDevicePairRepository

class DevicePairRepository : IDevicePairRepository {
    override suspend fun getStatus(): ResultWrapper<DevicePairStatus> {
        TODO("Not yet implemented")
    }

    override suspend fun getNetworks(): ResultWrapper<List<DevicePairWifiScanResult>> {
        TODO("Not yet implemented")
    }

    override suspend fun connect(
        ssid: String,
        password: String?,
        setupToken: String
    ): ResultWrapper<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun pair(dsn: String, setupToken: String): ResultWrapper<DevicePairStatus> {
        TODO("Not yet implemented")
    }
}