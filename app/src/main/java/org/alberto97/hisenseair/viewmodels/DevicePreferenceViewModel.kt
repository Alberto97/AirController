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

    init {
        viewModelScope.launch {
            updateDeviceProps()
            fetchTempType()
        }
    }

    fun switchTempType() {
        val type =
            if (useCelsius.value)
                TempType.Fahrenheit
            else
                TempType.Celsius

        viewModelScope.launch(Dispatchers.IO) {
            deviceControl.setTempUnit(dsn, type)
            fetchTempType()
        }
    }

    fun deleteDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteDevice(dsn)
            _popToHome.value = true
        }
    }

    fun updateDeviceName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setDeviceName(name, dsn)
            updateDeviceProps()
        }
    }

    private suspend fun fetchTempType() {
        val value = deviceControl.getTempUnit(dsn)
        _useCelsius.value = value == TempType.Celsius
    }

    private suspend fun updateDeviceProps() {
        val dev = repo.getDevice(dsn)
        _deviceName.value = dev.name
        _ip.value = dev.lanIp
        _mac.value = dev.mac
        _ssid.value = Uri.decode(dev.ssid)
    }

}