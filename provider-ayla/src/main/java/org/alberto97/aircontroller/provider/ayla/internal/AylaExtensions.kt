package org.alberto97.aircontroller.provider.ayla.internal

import com.squareup.moshi.Moshi
import org.alberto97.aircontroller.provider.ayla.internal.models.Device
import org.alberto97.aircontroller.provider.ayla.internal.models.Response
import org.alberto97.aircontroller.provider.ayla.internal.models.WifiScanResults
import org.alberto97.aircontroller.provider.ayla.serialization.MoshiAylaExtensions.addAyla
import retrofit2.HttpException

internal object AylaExtensions {

    fun Device.isAvailable(): Boolean {
        return this.connectionStatus == AylaDeviceState.ONLINE
    }

    fun WifiScanResults.WifiScanResult.isOpen(): Boolean {
        return security == "None"
    }

    fun HttpException.aylaError(): String? {
        val text = this.response()?.errorBody()?.charStream()?.readText() ?: ""
        val moshi = Moshi.Builder().addAyla().build()
        val adapter = moshi.adapter(Response::class.java)
        val response = adapter.fromJson(text)
        return response?.error
    }
}