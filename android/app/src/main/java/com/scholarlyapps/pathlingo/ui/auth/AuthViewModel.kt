package com.scholarlyapps.pathlingo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val user: AppUserDto? = null,
    val success: Boolean = false,
)

class AuthViewModel : ViewModel() {

    private val repo = ServiceLocator.authRepository

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String, deviceName: String) {
        launchAuth { repo.login(email, password, deviceName) }
    }

    fun register(name: String, email: String, password: String, confirm: String, deviceName: String) {
        launchAuth { repo.register(name, email, password, confirm, deviceName) }
    }

    fun loginWithGoogle(idToken: String, deviceName: String) {
        launchAuth { repo.loginWithGoogle(idToken, deviceName) }
    }

    fun consumeError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun launchAuth(block: suspend () -> Result<AppUserDto>) {
        _state.value = AuthUiState(loading = true)
        viewModelScope.launch {
            val result = block()
            _state.value = result.fold(
                onSuccess = { AuthUiState(user = it, success = true) },
                onFailure = { AuthUiState(error = it.message ?: "Something went wrong.") },
            )
        }
    }
}
