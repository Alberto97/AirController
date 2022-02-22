package org.alberto97.aircontroller.ui.devicesettings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.aircontroller.common.features.TempType
import org.alberto97.aircontroller.common.models.AppDevice
import org.alberto97.aircontroller.common.models.ResultWrapper
import org.alberto97.aircontroller.models.ScreenState
import org.alberto97.aircontroller.provider.repositories.IDeviceRepository
import org.alberto97.aircontroller.utils.IDeviceShortcutManager

class DeviceSettingsViewModel(
    private val dsn: String,
    private val repo: IDeviceRepository,
    private val shortcutManager: IDeviceShortcutManager
) : ViewModel() {

    private val _state = MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

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
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ScreenState.Loading
            val res1 = updateDeviceProps()
            val res2 = fetchTempType()
            _state.value = if (res1 is ResultWrapper.Error || res2 is ResultWrapper.Error)
                ScreenState.Error
            else
                ScreenState.Success
        }
    }

    fun switchTempType() {
        val type = if (useCelsius.value) TempType.Fahrenheit else TempType.Celsius

        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.setTempUnit(dsn, type)
            if (result.data != null)
                fetchTempType()
            else
                _message.value = result.message
        }
    }

    fun deleteDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.deleteDevice(dsn)
            if (result.data != null) {
                shortcutManager.removeShortcut(dsn)
                _popToHome.value = true
            } else {
                _message.value = result.message
            }
        }
    }

    fun updateDeviceName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.setDeviceName(name, dsn)
            if (result.data != null) {
                shortcutManager.updateShortcut(name, dsn)
                updateDeviceProps()
            } else {
                _message.value = result.message
            }
        }
    }

    private suspend fun fetchTempType(): ResultWrapper<TempType> {
        val result = repo.getTempUnit(dsn)
        if (result.data != null)
            _useCelsius.value = result.data == TempType.Celsius
        else
            _message.value = result.message

        return result
    }

    private suspend fun updateDeviceProps(): ResultWrapper<AppDevice> {
        val result = repo.getDevice(dsn)
        val data = result.data
        if (data == null) {
            _message.value = result.message
            return result
        }

        _deviceName.value = data.name
        _ip.value = data.lanIp
        _mac.value = data.mac
        _ssid.value = Uri.decode(data.ssid)

        return result
    }

    fun clearMessage() {
        _message.value = ""
    }
}