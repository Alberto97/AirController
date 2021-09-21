package org.alberto97.hisenseair.provider.ayla.repositories

import android.util.Log
import org.alberto97.hisenseair.common.models.DevicePairStatus
import org.alberto97.hisenseair.common.models.DevicePairWifiScanResult
import org.alberto97.hisenseair.common.models.ResultWrapper
import org.alberto97.hisenseair.common.enums.WifiSecurity
import org.alberto97.hisenseair.provider.ayla.internal.AylaExtensions.isOpen
import org.alberto97.hisenseair.provider.ayla.internal.models.SetupDevice
import org.alberto97.hisenseair.provider.ayla.internal.network.api.AylaService
import org.alberto97.hisenseair.provider.ayla.internal.network.api.PairApi
import org.alberto97.hisenseair.provider.repositories.IDevicePairRepository

internal class DevicePairRepository(
    private val pairApi: PairApi,
    private val aylaApi: AylaService
): IDevicePairRepository {
    companion object {
        private const val LOG_TAG = "HiPair"
    }

    override suspend fun getStatus(): ResultWrapper<DevicePairStatus> {
        Log.d(LOG_TAG, "Retrieving device status")
        return try {
            val resp = pairApi.status()
            val model = DevicePairStatus(resp.dsn, "")
            ResultWrapper.Success(model)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot retrieve device status")
        }
    }

    override suspend fun getNetworks(): ResultWrapper<List<DevicePairWifiScanResult>> {
        // Scan for networks on the device
        Log.d(LOG_TAG, "Device scanning wifi networks")
        val scanResp = pairApi.wifiScan()
        if (!scanResp.isSuccessful) {
            Log.e(LOG_TAG, scanResp.code().toString())
            return ResultWrapper.Error("Network scanning failure")
        }

        // Retrieve found networks
        Log.d(LOG_TAG, "Retrieve found wifi networks")
        return try {
            val results = pairApi.wifiScanResults()
            val list = results.wifiScan.results.map { network ->
                val security = if (network.isOpen()) WifiSecurity.OPEN else WifiSecurity.PROTECTED
                DevicePairWifiScanResult(network.ssid, security, network.bars)
            }
            ResultWrapper.Success(list)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot retrieve network scan result")
        }
    }

    override suspend fun connect(ssid: String, password: String?, setupToken: String): ResultWrapper<Unit> {
        Log.d(LOG_TAG, "Connecting to the wifi network")
        val resp = pairApi.wifiConnect(ssid, password, setupToken)
        return if (!resp.isSuccessful) {
            Log.e(LOG_TAG, resp.code().toString())
            ResultWrapper.Error("The entered password might be wrong. Cannot connect to the wifi network")
        } else {
            ResultWrapper.Success(Unit)
        }
    }

    override suspend fun pair(dsn: String, setupToken: String): ResultWrapper<DevicePairStatus> {
        Log.d(LOG_TAG, "Notify server the device has been connected")
        try {
            aylaApi.connected(dsn, setupToken)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            return ResultWrapper.Error("Cannot notify the server a new device has been connected")
        }

        Log.d(LOG_TAG, "Pair device to account")
        return try {
            val setupDevice = SetupDevice(dsn, setupToken)
            val wrapper = SetupDevice.Wrapper(setupDevice)
            val resp = aylaApi.postDevice(wrapper)
            val model = DevicePairStatus(resp.device.dsn, resp.device.productName)
            ResultWrapper.Success(model)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot pair device to the account")
        }
    }
}
