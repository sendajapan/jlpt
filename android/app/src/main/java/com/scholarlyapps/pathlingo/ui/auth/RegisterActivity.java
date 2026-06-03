package com.scholarlyapps.pathlingo.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.BuildConfig;
import com.scholarlyapps.pathlingo.databinding.ActivityRegisterBinding;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;
    private GoogleAuthClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        googleClient = new GoogleAuthClient(this, BuildConfig.GOOGLE_WEB_CLIENT_ID);

        binding.btnRegister.setOnClickListener(v -> submitRegister());
        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());
        binding.btnLoginAsGuest.setOnClickListener(v -> viewModel.guestLogin(deviceName()));

        binding.btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            NavAnim.slideBack(this);
            finish();
        });

        viewModel.getStateLiveData().observe(this, state -> {
            boolean loading = state.getLoading();
            binding.btnRegister.setEnabled(!loading);
            binding.btnRegister.setText(loading ? "Creating…" : "Register");
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

            String error = state.getError();
            if (error != null && !error.isEmpty()) {
                ToastHelper.error(this, error);
            }

            if (state.getSuccess()) {
                goToOtpVerification();
            }
        });
    }

    private void submitRegister() {
        String name = binding.editName.getText() != null ? binding.editName.getText().toString().trim() : "";
        String email = binding.editEmail.getText() != null ? binding.editEmail.getText().toString().trim() : "";
        String password = binding.editPassword.getText() != null ? binding.editPassword.getText().toString() : "";
        String confirm = binding.editConfirm.getText() != null ? binding.editConfirm.getText().toString() : "";
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            ToastHelper.warning(this, "Please fill in all fields.");
            return;
        }
        if (!password.equals(confirm)) {
            ToastHelper.warning(this, "Passwords do not match.");
            return;
        }
        viewModel.register(name, email, password, confirm, deviceName());
    }

    private void signInWithGoogle() {
        googleClient.signIn(idToken -> viewModel.loginWithGoogle(idToken, deviceName()));
    }

    private void goToOtpVerification() {
        String email = binding.editEmail.getText() != null ? binding.editEmail.getText().toString().trim() : "";
        Intent intent = new Intent(this, OtpActivity.class);
        intent.putExtra(OtpActivity.EXTRA_EMAIL, email);
        startActivity(intent);
        NavAnim.slideForward(this);
        finish();
    }

    private static String deviceName() {
        return "android-" + Build.MODEL;
    }
}
