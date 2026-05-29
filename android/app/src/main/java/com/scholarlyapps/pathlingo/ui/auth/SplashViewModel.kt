package com.scholarlyapps.pathlingo.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _authenticated = MutableLiveData<Boolean>()
    val authenticated: LiveData<Boolean> = _authenticated

    fun checkAuth() {
        viewModelScope.launch {
            val token = ServiceLocator.authRepository.storedToken()
            if (!token.isNullOrEmpty() && ServiceLocator.userRepository.me().isSuccess) {
                _authenticated.value = true
            } else {
                if (!token.isNullOrEmpty()) ServiceLocator.tokenStore.clear()
                _authenticated.value = false
            }
        }
    }
}
