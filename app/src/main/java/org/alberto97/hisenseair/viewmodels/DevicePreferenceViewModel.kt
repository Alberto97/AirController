package org.alberto97.hisenseair.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.features.TEMP_TYPE_PROP
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.features.TempTypeMap
import org.alberto97.hisenseair.repositories.IDeviceRepository

class DevicePreferenceViewModel(private val repo: IDeviceRepository, val dsn: String) : ViewModel() {

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

    init  {
        viewModelScope.launch {
            updateDeviceProps()
            fetchTempType()
        }
    }

    fun switchTempType() {
        val type =
            if (tempType.value == TempType.Celsius)
                TempType.Fahrenheit
            else
                TempType.Celsius

        viewModelScope.launch {
            repo.setProperty(TEMP_TYPE_PROP, type.value, dsn)
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
        val resp = repo.getBooleanProperty(TEMP_TYPE_PROP, dsn)
        tempType.value = TempTypeMap[resp]
    }

    private suspend fun updateDeviceProps() {
        val dev = repo.getDevice(dsn)
        deviceName.value = dev.productName
        ip.value = dev.lanIp
        mac.value = dev.mac
        ssid.value = Uri.decode(dev.ssid)
    }

}