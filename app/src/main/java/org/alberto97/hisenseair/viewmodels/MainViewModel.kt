package org.alberto97.hisenseair.viewmodels

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.models.ScreenState
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.repositories.IDeviceRepository
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.utils.IDeviceShortcutManager


class MainViewModel(
    private val repo: IDeviceRepository,
    private val auth: IAuthenticationRepository,
    private val settings: ISettingsRepository,
    private val shortcutManager: IDeviceShortcutManager
) : ViewModel() {

    private val _devices = MutableStateFlow(emptyList<AppDevice>())
    val devices = _devices.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _state = MutableStateFlow(ScreenState.Loading)
    val state = _state.asStateFlow()

    private val isAtLeastQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val pairAvailable = _state.map {
        state.value == ScreenState.Success && isAtLeastQ
    }

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ScreenState.Loading

            val result = fetchData()
            _state.value = if (result is ResultWrapper.Success)
                ScreenState.Success
            else
                ScreenState.Error
        }
    }

    // In case of refresh we already have some data.
    // If the refresh fails it would not make much sense to block the user
    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchData()
        }
    }

    private suspend fun fetchData(): ResultWrapper<List<AppDevice>> {
        val result = repo.getDevices()
        if (result.data != null)
            _devices.value = result.data
        else
            _message.value = result.message

        return result
    }

    fun clearMessage() {
        _message.value = ""
    }

    fun logOut() {
        auth.logout()
        settings.region = null
        settings.provider = null
        shortcutManager.removeAllShortcuts()
    }
}