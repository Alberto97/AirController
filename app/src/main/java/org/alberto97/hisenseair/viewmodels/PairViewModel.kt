package org.alberto97.hisenseair.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.PatternMatcher
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.KotlinExtensions.availableNetworkCallback
import org.alberto97.hisenseair.ayla.AylaExtensions.isOpen
import org.alberto97.hisenseair.ayla.models.WifiScanResults
import org.alberto97.hisenseair.repositories.IDevicePairRepository

@RequiresApi(Build.VERSION_CODES.Q)
class PairViewModel(
    app: Application,
    private val repository: IDevicePairRepository
) : ViewModel() {

    enum class Steps {
        PickDevice,
        SelectNetwork,
        InsertPassword,
        Connecting,
        DevicePaired
    }

    private val pattern = "Hi-Smart-[0-9a-zA-Z]{12}"
    private val setupToken = getRandomString(8)
    private var dsn = ""

    private val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _currentStep = MutableStateFlow(Steps.PickDevice)
    val currentStep = _currentStep.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _messages = MutableStateFlow("")
    val messages = _messages.asStateFlow()

    private val _exit = MutableStateFlow(false)
    val exit = _exit.asStateFlow()

    private val _scanResults = MutableStateFlow(listOf<WifiScanResults.WifiScanResult>())
    val scanResults = _scanResults.asStateFlow()

    private val _selectedSsid = MutableStateFlow("")
    val selectedSsid = _selectedSsid.asStateFlow()

    private val _deviceName = MutableStateFlow("")
    val deviceName = _deviceName.asStateFlow()


    private val callback = availableNetworkCallback { network ->
        viewModelScope.launch(Dispatchers.IO) {
            connectivityManager.bindProcessToNetwork(network)

            getDsn()

            _currentStep.value = Steps.SelectNetwork

            scanNetwork()
        }
    }

    fun selectDeviceAp() {
        val specifier = WifiNetworkSpecifier.Builder()
            .setSsidPattern(PatternMatcher(pattern, PatternMatcher.PATTERN_ADVANCED_GLOB))
            .build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(specifier)
            .build()

        connectivityManager.requestNetwork(request, callback)
    }

    // Retrieve device dsn
    private suspend fun getDsn() {
        val status = repository.getStatus()
        if (status.data != null)
            dsn = status.data.dsn
        else if (status.message != null)
            handleFailure(status.message)
    }

    private suspend fun scanNetwork() {
        _loading.value = true

        val result = repository.getNetworks()
        if (result.data != null)
            _scanResults.value = result.data
        else if (result.message != null)
            handleFailure(result.message)

        _loading.value = false
    }

    fun setSelectedSsid(network: WifiScanResults.WifiScanResult) {
        _selectedSsid.value = network.ssid

        if (network.isOpen()) {
            connectDeviceToWifi()
        } else {
            _currentStep.value = Steps.InsertPassword
        }
    }

    fun connectDeviceToWifi(password: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            val result = repository.connect(_selectedSsid.value, password, setupToken)
            unregister()
            if (result.data != null) {
                requestInternetNetwork()
                _currentStep.value = Steps.Connecting
            } else if (result.message != null) {
                _messages.value = result.message
            }

            // TODO: Poll to stop ap?
            _loading.value = false
        }

    }

    private fun unregister() {
        // Unbind from network
        connectivityManager.bindProcessToNetwork(null)

        // Release the request when done.
        connectivityManager.unregisterNetworkCallback(callback)
    }

    private val callbackInternet = availableNetworkCallback { network ->
        viewModelScope.launch(Dispatchers.IO) {
            connectivityManager.bindProcessToNetwork(network)
            pairToAccount()
            unregisterInternet()
        }
    }

    private fun requestInternetNetwork() {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.requestNetwork(request, callbackInternet)
    }

    private suspend fun pairToAccount() {
        val device = repository.pair(dsn, setupToken)
        if (device.data != null) {
            _deviceName.value = device.data.productName
            _currentStep.value = Steps.DevicePaired
        } else if (device.message != null) {
            handleInternetFailure(device.message)
        }
    }

    private fun unregisterInternet() {
        // Unbind from network
        connectivityManager.bindProcessToNetwork(null)

        // Release the request when done.
        connectivityManager.unregisterNetworkCallback(callbackInternet)
    }

    fun clearMessage() {
        _messages.value = ""
    }

    @Suppress("SameParameterValue")
    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun handleFailure(message: String) {
        _messages.value = message
        unregister()
        _exit.value = true
    }

    private fun handleInternetFailure(message: String) {
        _messages.value = message
        unregister()
        _exit.value = true
    }
}