package org.alberto97.hisenseair.viewmodels

import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.features.TemperatureExtensions.isCelsius
import org.alberto97.hisenseair.fragments.DeviceSettingsArgs
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository

class DevicePreferenceViewModel(
    savedStateHandle: SavedStateHandle,
    private val repo: IDeviceRepository,
    private val deviceControl: IDeviceControlRepository
) : ViewModel() {

    private val args = DeviceSettingsArgs.fromSavedStateHandle(savedStateHandle)
    private val dsn get() = args.dsn

    val deviceName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val tempType: MutableLiveData<TempType> by lazy {
        MutableLiveData<TempType>()
    }

    val ip: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val mac: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val ssid: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val _popToHome = MutableLiveData(false)
    val popToHome: LiveData<Boolean> = _popToHome

    init {
        viewModelScope.launch {
            updateDeviceProps()
            fetchTempType()
        }
    }

    fun switchTempType() {
        val type =
            if (tempType.value!!.isCelsius())
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
            withContext(Dispatchers.Main) {
                _popToHome.value = true
            }
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
        withContext(Dispatchers.Main) {
            tempType.value = value
        }
    }

    private suspend fun updateDeviceProps() {
        val dev = repo.getDevice(dsn)
        withContext(Dispatchers.Main) {
            deviceName.value = dev.name
            ip.value = dev.lanIp
            mac.value = dev.mac
            ssid.value = Uri.decode(dev.ssid)
        }
    }

}