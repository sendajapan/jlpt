package com.scholarlyapps.pathlingo.ui.auth

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import com.scholarlyapps.pathlingo.BuildConfig
import kotlinx.coroutines.launch

private fun deviceName(): String = "android-${Build.MODEL ?: "device"}"

@Composable
fun LoginScreen(
    onAuthenticated: () -> Unit,
    onNavigateSignup: () -> Unit,
    viewModel: AuthViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleClient = remember { GoogleAuthClient(context, BuildConfig.GOOGLE_WEB_CLIENT_ID) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state.success) { if (state.success) onAuthenticated() }

    Scaffold { padding ->
        AuthFormColumn(padding) {
            Text("Welcome back", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(20.dp))
            Button(
                enabled = !state.loading,
                onClick = { viewModel.login(email.trim(), password, deviceName()) },
                modifier = Modifier.fillMaxWidth(),
            ) { Text(if (state.loading) "Signing in…" else "Sign in") }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                enabled = !state.loading,
                onClick = {
                    scope.launch {
                        googleClient.signIn()
                            .onSuccess { viewModel.loginWithGoogle(it, deviceName()) }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Continue with Google") }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateSignup) { Text("Create an account") }

            ErrorText(state.error)
            if (state.loading) {
                Spacer(Modifier.height(12.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SignupScreen(
    onAuthenticated: () -> Unit,
    onNavigateLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleClient = remember { GoogleAuthClient(context, BuildConfig.GOOGLE_WEB_CLIENT_ID) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    LaunchedEffect(state.success) { if (state.success) onAuthenticated() }

    Scaffold { padding ->
        AuthFormColumn(padding) {
            Text("Create your account", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = confirm,
                onValueChange = { confirm = it },
                label = { Text("Confirm password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(20.dp))
            Button(
                enabled = !state.loading,
                onClick = {
                    viewModel.register(name.trim(), email.trim(), password, confirm, deviceName())
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text(if (state.loading) "Creating…" else "Create account") }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                enabled = !state.loading,
                onClick = {
                    scope.launch {
                        googleClient.signIn()
                            .onSuccess { viewModel.loginWithGoogle(it, deviceName()) }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Continue with Google") }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateLogin) { Text("Already have an account? Sign in") }

            ErrorText(state.error)
            if (state.loading) {
                Spacer(Modifier.height(12.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun AuthFormColumn(padding: PaddingValues, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) { content() }
}

@Composable
private fun ErrorText(error: String?) {
    if (!error.isNullOrEmpty()) {
        Spacer(Modifier.height(12.dp))
        Text(error, color = MaterialTheme.colorScheme.error)
    }
}
