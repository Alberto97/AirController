package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import org.alberto97.hisenseair.repositories.IAuthenticationRepository

class SplashViewModel(private val repo: IAuthenticationRepository) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return repo.getToken().isNotEmpty()
    }
}