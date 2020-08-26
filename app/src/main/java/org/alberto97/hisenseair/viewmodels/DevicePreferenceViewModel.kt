package org.alberto97.hisenseair.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.features.TemperatureExtensions.isCelsius
import org.alberto97.hisenseair.repositories.IDeviceRepository

class DevicePreferenceViewModel(private val repo: IDeviceRepository) : ViewModel() {

    var dsn: String = ""

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

    fun load(dsn: String) {
        this.dsn = dsn
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

        viewModelScope.launch {
            repo.setTempUnit(dsn, type)
            fetchTempType()
        }
    }

    fun updateDeviceName(name: String) {
        viewModelScope.launch {
            repo.setDeviceName(name, dsn)
            updateDeviceProps()
        }
    }

    private suspend fun fetchTempType() {
        tempType.value = repo.getTempUnit(dsn)
    }

    private suspend fun updateDeviceProps() {
        val dev = repo.getDevice(dsn)
        deviceName.value = dev.productName
        ip.value = dev.lanIp
        mac.value = dev.mac
        ssid.value = Uri.decode(dev.ssid)
    }

}