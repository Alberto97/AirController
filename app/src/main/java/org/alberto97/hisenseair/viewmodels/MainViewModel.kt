package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.models.Device
import org.alberto97.hisenseair.repositories.IDeviceControlRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

class MainViewModel(
    private val repo : IDeviceRepository,
    private val deviceControl: IDeviceControlRepository) : ViewModel() {

    val devices: MutableLiveData<List<AppDevice>> by lazy {
        MutableLiveData<List<AppDevice>>()
    }

    val isLoggedOut: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                val deviceList = repo.getDevices()
                devices.value = deviceList.map { fetchDeviceData(it) }
            } catch (e: HttpException) {
                isLoggedOut.value = e.code() == HttpsURLConnection.HTTP_UNAUTHORIZED
            }
        }
    }

    private suspend fun fetchDeviceData(it: Device): AppDevice {
        val resp = deviceControl.getDeviceState(it.dsn)
        return AppDevice(it.dsn, it.productName, "${resp.temp}°", resp.on, resp.workMode)
    }

}