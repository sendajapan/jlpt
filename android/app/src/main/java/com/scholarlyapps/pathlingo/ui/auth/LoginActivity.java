package com.scholarlyapps.pathlingo.ui.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.scholarlyapps.pathlingo.BuildConfig;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.ui.activities.MainDashboardActivity;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    private GoogleAuthClient googleClient;

    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        googleClient = new GoogleAuthClient(this, BuildConfig.GOOGLE_WEB_CLIENT_ID);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> submitLogin());

        findViewById(R.id.btnGoogle).setOnClickListener(v -> signInWithGoogle());

        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            NavAnim.slideForward(this);
        });

        viewModel.getStateLiveData().observe(this, state -> {
            boolean loading = state.getLoading();
            btnLogin.setEnabled(!loading);
            btnLogin.setText(loading ? "Signing in…" : "Login");
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

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
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String password = editPassword.getText() != null ? editPassword.getText().toString() : "";
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
        DataManager.getInstance().loadData(getApplicationContext());
        startActivity(new Intent(this, MainDashboardActivity.class));
        NavAnim.slideForward(this);
        finish();
    }

    private static String deviceName() {
        return "android-" + Build.MODEL;
    }
}
