package org.alberto97.hisenseair.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    fun login(email: String, password: String) {
        _state.value = LoginState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val success = repo.login(email, password)
            if (success) {
                _state.value = LoginState.Success
            } else {
                _state.value = LoginState.Error
                _message.value = "Authentication failed"
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}