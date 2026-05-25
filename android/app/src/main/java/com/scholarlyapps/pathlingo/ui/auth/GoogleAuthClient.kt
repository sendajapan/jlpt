package com.scholarlyapps.pathlingo.ui.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun interface GoogleSignInCallback {
    fun onSuccess(idToken: String)
}

class GoogleAuthClient(
    private val context: Context,
    private val webClientId: String,
) {

    private val credentialManager = CredentialManager.create(context)

    fun signIn(callback: GoogleSignInCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            signIn().onSuccess { callback.onSuccess(it) }
        }
    }

    suspend fun signIn(): Result<String> = runCatching {
        require(webClientId.isNotEmpty()) {
            "Missing GOOGLE_WEB_CLIENT_ID. Set it in android/gradle.properties."
        }
        val option = GetSignInWithGoogleOption.Builder(webClientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()

        val response = credentialManager.getCredential(context, request)
        val credential = response.credential
        require(credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            "Unexpected credential type: ${credential::class.java.name}"
        }
        GoogleIdTokenCredential.createFrom(credential.data).idToken
    }
}
