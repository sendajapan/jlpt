package com.scholarlyapps.pathlingo.data.repo

import com.scholarlyapps.pathlingo.data.local.TokenStore
import com.scholarlyapps.pathlingo.data.remote.ApiService
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto
import com.scholarlyapps.pathlingo.data.remote.dto.ForgotPasswordRequest
import com.scholarlyapps.pathlingo.data.remote.dto.GoogleLoginRequest
import com.scholarlyapps.pathlingo.data.remote.dto.LoginRequest
import com.scholarlyapps.pathlingo.data.remote.dto.RegisterRequest

class AuthRepository(
    private val api: ApiService,
    private val tokenStore: TokenStore,
) {

    suspend fun login(email: String, password: String, deviceName: String): Result<AppUserDto> = runCatching {
        val response = api.login(LoginRequest(email, password, deviceName))
        tokenStore.save(response.token)
        response.user
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        deviceName: String,
    ): Result<AppUserDto> = runCatching {
        val response = api.register(
            RegisterRequest(name, email, password, passwordConfirmation, deviceName),
        )
        tokenStore.save(response.token)
        response.user
    }

    suspend fun loginWithGoogle(idToken: String, deviceName: String): Result<AppUserDto> = runCatching {
        val response = api.loginWithGoogle(GoogleLoginRequest(idToken, deviceName))
        tokenStore.save(response.token)
        response.user
    }

    suspend fun forgotPassword(email: String): Result<String?> = runCatching {
        api.forgotPassword(ForgotPasswordRequest(email)).message
    }

    suspend fun logout(): Result<Unit> = runCatching {
        runCatching { api.logout() }
        tokenStore.clear()
    }

    suspend fun storedToken(): String? = tokenStore.token()
}
