package org.alberto97.aircontroller.provider.ayla.internal

import com.google.gson.Gson
import org.alberto97.aircontroller.provider.ayla.internal.models.Device
import org.alberto97.aircontroller.provider.ayla.internal.models.Response
import org.alberto97.aircontroller.provider.ayla.internal.models.WifiScanResults
import retrofit2.HttpException

internal object AylaExtensions {

    fun Device.isAvailable(): Boolean {
        return this.connectionStatus == AylaDeviceState.ONLINE
    }

    fun WifiScanResults.WifiScanResult.isOpen(): Boolean {
        return security == "None"
    }

    fun HttpException.aylaError(): String? {
        val text = this.response()?.errorBody()?.charStream()?.readText()
        val response = Gson().fromJson(text, Response::class.java)
        return response.error
    }
}