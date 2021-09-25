package org.alberto97.aircontroller.connectivity

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.PatternMatcher
import androidx.annotation.RequiresApi
import org.alberto97.aircontroller.KotlinExtensions.availableNetworkCallback
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface IPairConnectivityManager {
    suspend fun connectDevice()
    fun disconnectDevice()
    suspend fun connectWifi()
    fun disconnectWifi()
}

class PairConnectivityManager(app: Application) : IPairConnectivityManager {

    private val pattern = "Hi-Smart-[0-9a-zA-Z]{12}"

    private val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var deviceCallback: ConnectivityManager.NetworkCallback? = ConnectivityManager.NetworkCallback()
    private var wifiCallback: ConnectivityManager.NetworkCallback? = ConnectivityManager.NetworkCallback()

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun connectDevice() {
        val specifier = WifiNetworkSpecifier.Builder()
            .setSsidPattern(PatternMatcher(pattern, PatternMatcher.PATTERN_ADVANCED_GLOB))
            .build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(specifier)
            .build()

        suspendCoroutine<Unit?> {
            deviceCallback = availableNetworkCallback { network ->
                connectivityManager.bindProcessToNetwork(network)
                it.resume(null)
            }
            connectivityManager.requestNetwork(request, deviceCallback!!)
        }
    }

    override fun disconnectDevice() {
        // Unbind from network
        connectivityManager.bindProcessToNetwork(null)

        // Release the request when done.
        if (deviceCallback != null)
            connectivityManager.unregisterNetworkCallback(deviceCallback!!)

        deviceCallback = null
    }


    override suspend fun connectWifi() {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        suspendCoroutine<Unit?> {
            wifiCallback = availableNetworkCallback { network ->
                connectivityManager.bindProcessToNetwork(network)
                it.resume(null)
            }
            connectivityManager.requestNetwork(request, wifiCallback!!)
        }

    }

    override fun disconnectWifi() {
        // Unbind from network
        connectivityManager.bindProcessToNetwork(null)

        // Release the request when done.
        if (wifiCallback != null)
            connectivityManager.unregisterNetworkCallback(wifiCallback!!)

        wifiCallback = null
    }
}