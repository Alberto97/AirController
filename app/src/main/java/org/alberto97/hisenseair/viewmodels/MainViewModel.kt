package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.repositories.IDeviceRepository
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

class MainViewModel(private val repo : IDeviceRepository) : ViewModel() {

    private val _devices = MutableStateFlow(emptyList<AppDevice>())
    val devices = _devices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut = _isLoggedOut.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            fetchData()
            _isLoading.value = false
        }
    }

    private suspend fun fetchData() {
        try {
            val deviceList = repo.getDevices()
            _devices.value = deviceList
        } catch (e: HttpException) {
            _isLoggedOut.value = e.code() == HttpsURLConnection.HTTP_UNAUTHORIZED
        }
    }
}