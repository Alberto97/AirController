package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.repositories.IAuthenticationRepository

class LoginViewModel(private val repo: IAuthenticationRepository) : ViewModel() {

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isAuthenticated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun login(email: String, password: String) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val success = repo.login(email, password)
            withContext(Dispatchers.Main) {
                isAuthenticated.value = success
                isLoading.value = false
            }
        }
    }
}