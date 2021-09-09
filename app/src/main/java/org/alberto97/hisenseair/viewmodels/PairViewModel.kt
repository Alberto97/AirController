package org.alberto97.hisenseair.viewmodels

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.ayla.AylaExtensions.isOpen
import org.alberto97.hisenseair.ayla.models.WifiScanResults
import org.alberto97.hisenseair.connectivity.IPairConnectivityManager
import org.alberto97.hisenseair.repositories.IDevicePairRepository

class PairViewModel(
    private val repository: IDevicePairRepository,
    private val connManager: IPairConnectivityManager
) : ViewModel() {

    enum class Steps {
        PickDevice,
        SelectNetwork,
        InsertPassword,
        Connecting,
        DevicePaired
    }

    private val setupToken = getRandomString(8)
    private var dsn = ""

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


    fun selectDeviceAp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            throw Exception("Device pairing is unsupported on < Q")
        }

        viewModelScope.launch(Dispatchers.IO) {
            connManager.connectDevice()

            getDsn()

            _currentStep.value = Steps.SelectNetwork

            scanNetwork()
        }
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
            connManager.disconnectDevice()
            if (result.data != null) {
                _currentStep.value = Steps.Connecting
                connManager.connectWifi()
                pairToAccount()
                connManager.disconnectWifi()
            } else if (result.message != null) {
                _messages.value = result.message
            }

            // TODO: Poll to stop ap?
            _loading.value = false
        }
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
        connManager.disconnectDevice()
        _exit.value = true
    }

    private fun handleInternetFailure(message: String) {
        _messages.value = message
        connManager.disconnectWifi()
        _exit.value = true
    }
}