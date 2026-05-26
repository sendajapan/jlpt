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

public class RegisterActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    private GoogleAuthClient googleClient;

    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private TextInputEditText editConfirm;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        googleClient = new GoogleAuthClient(this, BuildConfig.GOOGLE_WEB_CLIENT_ID);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editConfirm = findViewById(R.id.editConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnRegister.setOnClickListener(v -> submitRegister());

        findViewById(R.id.btnGoogle).setOnClickListener(v -> signInWithGoogle());

        findViewById(R.id.btnLoginAsGuest).setOnClickListener(v -> viewModel.guestLogin(deviceName()));

        findViewById(R.id.btnSignIn).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            NavAnim.slideBack(this);
            finish();
        });

        viewModel.getStateLiveData().observe(this, state -> {
            boolean loading = state.getLoading();
            btnRegister.setEnabled(!loading);
            btnRegister.setText(loading ? "Creating…" : "Register");
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

            String error = state.getError();
            if (error != null && !error.isEmpty()) {
                ToastHelper.error(this, error);
            }

            if (state.getSuccess()) {
                ToastHelper.success(this, "Account created successfully!");
                goToDashboard();
            }
        });
    }

    private void submitRegister() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String password = editPassword.getText() != null ? editPassword.getText().toString() : "";
        String confirm = editConfirm.getText() != null ? editConfirm.getText().toString() : "";
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
