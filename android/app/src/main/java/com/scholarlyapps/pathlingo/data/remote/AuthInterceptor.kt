package com.scholarlyapps.pathlingo.data.remote

import com.scholarlyapps.pathlingo.data.local.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
            .header("Accept", "application/json")

        if (!isPublicAuthEndpoint(request.url.encodedPath)) {
            val token = runBlocking { tokenStore.token() }
            if (!token.isNullOrEmpty()) {
                builder.header("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(builder.build())
    }

    private fun isPublicAuthEndpoint(path: String): Boolean {
        return path.endsWith("/auth/login") ||
            path.endsWith("/auth/register") ||
            path.endsWith("/auth/guest") ||
            path.contains("/auth/social/") ||
            path.endsWith("/auth/forgot-password") ||
            path.endsWith("/auth/reset-password")
    }
}
