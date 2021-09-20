package org.alberto97.hisenseair.viewmodels

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.connectivity.IPairConnectivityManager
import org.alberto97.hisenseair.models.DevicePairWifiScanResult
import org.alberto97.hisenseair.models.WifiSecurity
import org.alberto97.hisenseair.repositories.IDevicePairRepository

class PairViewModel(
    private val repository: IDevicePairRepository,
    private val connManager: IPairConnectivityManager
) : ViewModel() {

    enum class NavAction {
        PickDevice,
        SelectNetwork,
        InsertPassword,
        Connecting,
        DevicePaired,
        Exit
    }

    private val setupToken = getRandomString(8)
    private var dsn = ""

    private val _navAction = MutableStateFlow(NavAction.PickDevice)
    val navAction = _navAction.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _scanResults = MutableStateFlow(listOf<DevicePairWifiScanResult>())
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

            _navAction.value = NavAction.SelectNetwork

            scanNetwork()
        }
    }

    // Retrieve device dsn
    private suspend fun getDsn() {
        val status = repository.getStatus()
        if (status.data != null)
            dsn = status.data.id
        else
            handleFailure(status.message)
    }

    private suspend fun scanNetwork() {
        _loading.value = true

        val result = repository.getNetworks()
        if (result.data != null)
            _scanResults.value = result.data
        else
            handleFailure(result.message)

        _loading.value = false
    }

    fun setSelectedSsid(network: DevicePairWifiScanResult) {
        _selectedSsid.value = network.ssid

        if (network.security == WifiSecurity.OPEN) {
            connectDeviceToWifi()
        } else {
            _navAction.value = NavAction.InsertPassword
        }
    }

    fun connectDeviceToWifi(password: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            val result = repository.connect(_selectedSsid.value, password, setupToken)
            connManager.disconnectDevice()
            if (result.data != null) {
                _navAction.value = NavAction.Connecting
                connManager.connectWifi()
                pairToAccount()
                connManager.disconnectWifi()
            } else {
                _message.value = result.message
            }

            // TODO: Poll to stop ap?
            _loading.value = false
        }
    }

    private suspend fun pairToAccount() {
        val device = repository.pair(dsn, setupToken)
        if (device.data != null) {
            _deviceName.value = device.data.name
            _navAction.value = NavAction.DevicePaired
        } else {
            handleInternetFailure(device.message)
        }
    }

    fun clearMessage() {
        _message.value = ""
    }

    @Suppress("SameParameterValue")
    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun handleFailure(message: String) {
        _message.value = message
        connManager.disconnectDevice()
        _navAction.value = NavAction.Exit
    }

    private fun handleInternetFailure(message: String) {
        _message.value = message
        connManager.disconnectWifi()
        _navAction.value = NavAction.Exit
    }
}