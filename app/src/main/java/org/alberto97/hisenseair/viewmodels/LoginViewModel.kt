package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.repositories.IAuthenticationRepository

class LoginViewModel(private val repo: IAuthenticationRepository) : ViewModel() {

    val isAuthenticated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isAuthenticated.value = repo.login(email, password)
        }
    }
}