package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.ayla.IAylaModuleLoader
import org.alberto97.hisenseair.models.ListItemModel
import org.alberto97.hisenseair.models.ResultWrapper
import org.alberto97.hisenseair.models.ScreenState
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.alberto97.hisenseair.repositories.Region
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel(
    private val settings: ISettingsRepository,
    private val moduleLoader: IAylaModuleLoader
) : ViewModel(), KoinComponent {

    // This needs to be evaluated lazily because when there is no region data the retrofit
    // service, which is a dependency of the auth repository, is not registered
    // and therefore the dependencies resolution would fail
    private val repo: IAuthenticationRepository by inject()

    private val _state = MutableStateFlow<ScreenState?>(null)
    val state = _state.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _region = MutableStateFlow<ListItemModel<Region>?>(null)
    val region = _region.asStateFlow()

    private val euRegion = ListItemModel(Region.EU, "Europe", R.drawable.ic_eu)
    private val usRegion = ListItemModel(Region.US, "United States", R.drawable.ic_us)

    val regions = listOf(euRegion, usRegion)

    init {
        when (settings.region) {
            Region.EU -> _region.value = euRegion
            Region.US -> _region.value = usRegion
        }
    }

    fun login(email: String, password: String) {
        _state.value = ScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.login(email, password)
            if (result is ResultWrapper.Success) {
                _state.value = ScreenState.Success
            } else {
                _state.value = ScreenState.Error
                _message.value = "Authentication failed"
            }
        }
    }

    fun setRegion(region: ListItemModel<Region>) {
        _region.value = region
        moduleLoader.load(region.value)
        // Once region and appsecrets are untied, move this on login success
        settings.region = region.value
    }

    fun clearMessage() {
        _message.value = ""
    }
}