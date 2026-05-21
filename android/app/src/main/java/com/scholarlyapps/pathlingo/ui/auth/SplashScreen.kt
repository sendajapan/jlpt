package com.scholarlyapps.pathlingo.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator

@Composable
fun SplashScreen(
    onAuthenticated: () -> Unit,
    onNeedsLogin: () -> Unit,
) {
    LaunchedEffect(Unit) {
        val token = ServiceLocator.authRepository.storedToken()
        if (!token.isNullOrEmpty() && ServiceLocator.userRepository.me().isSuccess) {
            onAuthenticated()
        } else {
            if (!token.isNullOrEmpty()) ServiceLocator.tokenStore.clear()
            onNeedsLogin()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("PathLingo", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))
        CircularProgressIndicator()
    }
}
