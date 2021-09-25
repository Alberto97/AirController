package org.alberto97.aircontroller

import android.net.ConnectivityManager
import android.net.Network

object KotlinExtensions {
    fun availableNetworkCallback(onAvailable: (network: Network) -> Unit): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) = onAvailable(network)
        }
    }
}