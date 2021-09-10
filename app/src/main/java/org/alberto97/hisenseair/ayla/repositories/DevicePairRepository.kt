package org.alberto97.hisenseair.ayla.repositories

import android.util.Log
import org.alberto97.hisenseair.ayla.models.Device
import org.alberto97.hisenseair.ayla.models.SetupDevice
import org.alberto97.hisenseair.ayla.models.Status
import org.alberto97.hisenseair.ayla.models.WifiScanResults
import org.alberto97.hisenseair.ayla.network.api.AylaService
import org.alberto97.hisenseair.ayla.network.api.PairApi
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.repositories.IDevicePairRepository

class DevicePairRepository(
    private val pairApi: PairApi,
    private val aylaApi: AylaService
): IDevicePairRepository {
    companion object {
        private const val LOG_TAG = "HiPair"
    }

    override suspend fun getStatus(): ResultWrapper<Status> {
        Log.d(LOG_TAG, "Retrieving device status")
        return try {
            val resp = pairApi.status()
            ResultWrapper.Success(resp)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot retrieve device status")
        }
    }

    override suspend fun getNetworks(): ResultWrapper<List<WifiScanResults.WifiScanResult>> {
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
            ResultWrapper.Success(results.wifiScan.results)
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

    override suspend fun pair(dsn: String, setupToken: String): ResultWrapper<Device> {
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
            ResultWrapper.Success(resp.device)
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.stackTraceToString())
            ResultWrapper.Error("Cannot pair device to the account")
        }
    }
}
