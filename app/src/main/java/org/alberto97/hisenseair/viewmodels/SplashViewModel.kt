package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import org.alberto97.hisenseair.repositories.ISettingsRepository

class SplashViewModel(private val repo: ISettingsRepository) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return repo.loggedIn && repo.region != null
    }
}