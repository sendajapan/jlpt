package com.scholarlyapps.pathlingo.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _stateLiveData = MutableLiveData(AuthUiState())
    val stateLiveData: LiveData<AuthUiState> = _stateLiveData

    fun login(email: String, password: String, deviceName: String) {
        launchAuth { repo.login(email, password, deviceName) }
    }

    fun register(name: String, email: String, password: String, confirm: String, deviceName: String) {
        launchAuth { repo.register(name, email, password, confirm, deviceName) }
    }

    fun guestLogin(deviceName: String) {
        launchAuth { repo.guestLogin(deviceName) }
    }

    fun loginWithGoogle(idToken: String, deviceName: String) {
        launchAuth { repo.loginWithGoogle(idToken, deviceName) }
    }

    fun consumeError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun launchAuth(block: suspend () -> Result<AppUserDto>) {
        val loading = AuthUiState(loading = true)
        _state.value = loading
        _stateLiveData.value = loading
        viewModelScope.launch {
            val next = block().fold(
                onSuccess = { AuthUiState(user = it, success = true) },
                onFailure = { AuthUiState(error = it.message ?: "Something went wrong.") },
            )
            _state.value = next
            _stateLiveData.postValue(next)
        }
    }
}
