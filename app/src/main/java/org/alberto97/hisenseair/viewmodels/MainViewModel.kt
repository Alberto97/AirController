package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.repositories.IDeviceRepository

class MainViewModel(private val repo : IDeviceRepository) : ViewModel() {

    private val _devices = MutableStateFlow(emptyList<AppDevice>())
    val devices = _devices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

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
        val deviceList = repo.getDevices()
        _devices.value = deviceList
    }
}