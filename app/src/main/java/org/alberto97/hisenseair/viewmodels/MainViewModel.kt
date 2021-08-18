package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.repositories.IDeviceRepository
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

class MainViewModel(private val repo : IDeviceRepository) : ViewModel() {

    val devices: MutableLiveData<List<AppDevice>> by lazy {
        MutableLiveData<List<AppDevice>>()
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isLoggedOut: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading.value = true
            withContext(Dispatchers.IO) {
                fetchData()
            }
            isLoading.value = false
        }
    }

    private suspend fun fetchData() {
        try {
            val deviceList = repo.getDevices()
            withContext(Dispatchers.Main) {
                devices.value = deviceList
            }
        } catch (e: HttpException) {
            isLoggedOut.value = e.code() == HttpsURLConnection.HTTP_UNAUTHORIZED
        }
    }
}