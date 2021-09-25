package org.alberto97.aircontroller.provider.demo.repositories

import org.alberto97.aircontroller.common.models.DevicePairStatus
import org.alberto97.aircontroller.common.models.DevicePairWifiScanResult
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.provider.repositories.IDevicePairRepository

internal class DevicePairRepository : IDevicePairRepository {
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