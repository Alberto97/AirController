package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.hisenseair.repositories.IAuthenticationRepository

enum class LoginState {
    None,
    Loading,
    Success,
    Error
}

class LoginViewModel(private val repo: IAuthenticationRepository) : ViewModel() {


    private val _state = MutableStateFlow(LoginState.None)
    val state = _state.asStateFlow()

    fun login(email: String, password: String) {
        _state.value = LoginState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val success = repo.login(email, password)
            withContext(Dispatchers.Main) {
                _state.value = if (success)
                    LoginState.Success
                else
                    LoginState.Error
            }
        }
    }
}