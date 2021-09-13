package org.alberto97.hisenseair.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.repositories.IAuthenticationRepository
import org.alberto97.hisenseair.repositories.ISettingsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SplashViewModel(
    app: Application,
    private val settings: ISettingsRepository,
) : ViewModel(), KoinComponent {
    private val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // This needs to be evaluated lazily because when there is no region data the retrofit
    // service, which is a dependency of the auth repository, is not registered
    // and therefore the dependencies resolution would fail
    private val auth: IAuthenticationRepository by inject()

    enum class SplashNavAction {
        Main,
        Login
    }

    private val _navAction = MutableStateFlow<SplashNavAction?>(null)
    val navAction = _navAction.asStateFlow()

    private val _showOfflineMessage = MutableStateFlow(false)
    val showOfflineMessage = _showOfflineMessage.asStateFlow()

    init {
        load()
    }
    
    fun load() {
        if (!isConnected()) {
            _showOfflineMessage.value = true
            return
        }

        if (!hasLoginData()) {
            _navAction.value = SplashNavAction.Login
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (auth.refreshToken())
                _navAction.value = SplashNavAction.Main
            else
                _navAction.value = SplashNavAction.Login
        }
    }

    private fun hasLoginData(): Boolean {
        return settings.loggedIn && settings.region != null
    }

    private fun isConnected(): Boolean {
        val cap = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}