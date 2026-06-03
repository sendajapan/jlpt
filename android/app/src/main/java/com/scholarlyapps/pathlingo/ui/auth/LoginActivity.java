package com.scholarlyapps.pathlingo.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.BuildConfig;
import com.scholarlyapps.pathlingo.databinding.ActivityLoginBinding;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;
import com.scholarlyapps.pathlingo.ui.welcome.OnboardingActivity;
import com.scholarlyapps.pathlingo.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;
    private GoogleAuthClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().setNavigationBarColor(android.graphics.Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        googleClient = new GoogleAuthClient(this, BuildConfig.GOOGLE_WEB_CLIENT_ID);

        binding.btnLogin.setOnClickListener(v -> submitLogin());
        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());
        binding.btnLoginAsGuest.setOnClickListener(v -> viewModel.guestLogin(deviceName()));

        binding.btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            NavAnim.slideForward(this);
        });

        viewModel.getStateLiveData().observe(this, state -> {
            boolean loading = state.getLoading();
            binding.btnLogin.setEnabled(!loading);
            binding.btnLogin.setText(loading ? "Signing in…" : "Login");
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

            String error = state.getError();
            if (error != null && !error.isEmpty()) {
                ToastHelper.error(this, error);
            }

            if (state.getSuccess()) {
                ToastHelper.success(this, "Logged in successfully!");
                goToDashboard();
            }
        });
    }

    private void submitLogin() {
        String email = binding.editEmail.getText() != null ? binding.editEmail.getText().toString().trim() : "";
        String password = binding.editPassword.getText() != null ? binding.editPassword.getText().toString() : "";
        if (email.isEmpty() || password.isEmpty()) {
            ToastHelper.warning(this, "Please fill in all fields.");
            return;
        }
        viewModel.login(email, password, deviceName());
    }

    private void signInWithGoogle() {
        googleClient.signIn(idToken -> viewModel.loginWithGoogle(idToken, deviceName()));
    }

    private void goToDashboard() {
        startActivity(new Intent(this, OnboardingActivity.class));
        NavAnim.slideForward(this);
        finish();
    }

    private static String deviceName() {
        return "android-" + Build.MODEL;
    }
}
