package org.alberto97.hisenseair.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository

class DevicePreferenceViewModel(
    private val dsn: String,
    private val repo: IDeviceRepository,
    private val deviceControl: IDeviceControlRepository
) : ViewModel() {

    private val _deviceName = MutableStateFlow("")
    val deviceName = _deviceName.asStateFlow()

    private val _useCelsius = MutableStateFlow(false)
    val useCelsius = _useCelsius.asStateFlow()

    private val _ip = MutableStateFlow("")
    val ip = _ip.asStateFlow()

    private val _mac = MutableStateFlow("")
    val mac = _mac.asStateFlow()

    private val _ssid = MutableStateFlow("")
    val ssid = _ssid.asStateFlow()

    private val _popToHome = MutableStateFlow(false)
    val popToHome = _popToHome.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    init {
        viewModelScope.launch {
            updateDeviceProps()
            fetchTempType()
        }
    }

    fun switchTempType() {
        val type = if (useCelsius.value) TempType.Fahrenheit else TempType.Celsius

        viewModelScope.launch(Dispatchers.IO) {
            val result = deviceControl.setTempUnit(dsn, type)
            if (result.data != null)
                fetchTempType()
            else
                _message.value = result.message
        }
    }

    fun deleteDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.deleteDevice(dsn)
            if (result.data != null)
                _popToHome.value = true
            else
                _message.value = result.message
        }
    }

    fun updateDeviceName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.setDeviceName(name, dsn)
            if (result.data != null)
                updateDeviceProps()
            else
                _message.value = result.message
        }
    }

    private suspend fun fetchTempType() {
        val result = deviceControl.getTempUnit(dsn)
        if (result.data != null)
            _useCelsius.value = result.data == TempType.Celsius
        else
            _message.value = result.message
    }

    private suspend fun updateDeviceProps() {
        val result = repo.getDevice(dsn)
        if (result.data == null) {
            _message.value = result.message
            return
        }

        with(result.data) {
            _deviceName.value = name
            _ip.value = lanIp
            _mac.value = mac
            _ssid.value = Uri.decode(ssid)
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}