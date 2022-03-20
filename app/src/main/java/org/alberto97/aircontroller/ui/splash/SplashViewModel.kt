package org.alberto97.aircontroller.ui.splash

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.aircontroller.App
import org.alberto97.aircontroller.common.repositories.ISettingsRepository
import org.alberto97.aircontroller.provider.repositories.AuthErrorCodes
import org.alberto97.aircontroller.provider.repositories.IAuthenticationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SplashViewModel(
    private val app: Application,
    private val settings: ISettingsRepository,
) : ViewModel(), KoinComponent {

    // This needs to be evaluated lazily because when there is no region data the retrofit
    // service, which is a dependency of the auth repository, is not registered
    // and therefore the dependencies resolution would fail
    private val auth: IAuthenticationRepository by inject()

    enum class SplashNavAction {
        Main,
        Login,
        Oob
    }

    private val _navAction = MutableStateFlow<SplashNavAction?>(null)
    val navAction = _navAction.asStateFlow()

    private val _showOfflineMessage = MutableStateFlow(false)
    val showOfflineMessage = _showOfflineMessage.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            if (settings.oob) {
                navigateTo(SplashNavAction.Oob)
                return@launch
            }

            if (!hasLoginData()) {
                navigateTo(SplashNavAction.Login)
                return@launch
            }

            val resp = withContext(Dispatchers.IO) {
                auth.refreshToken()
            }

            // Only navigate to login when there is an authentication error
            // otherwise any error (eg: the lack of connectivity)
            // would lead to the login page which is unwanted
            if (resp.code == AuthErrorCodes.UNAUTHORIZED)
                navigateTo(SplashNavAction.Login)
            else
                navigateTo(SplashNavAction.Main)
        }
    }

    private suspend fun navigateTo(page: SplashNavAction) {
        _navAction.value = page
        // Wait a little bit to make sure the [composable] splash screen has gone away
        delay(100L)
        (app as App).showSplashScreen = false
    }

    private fun hasLoginData(): Boolean {
        return settings.loggedIn && settings.region != null
    }
}