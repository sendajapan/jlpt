package com.scholarlyapps.pathlingo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.databinding.ActivityOtpBinding;
import com.scholarlyapps.pathlingo.ui.activities.MainDashboardActivity;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;

import java.util.Locale;

public class OtpActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "extra_email";
    private static final long RESEND_COOLDOWN_MS = 60_000L;

    private ActivityOtpBinding binding;
    private AuthViewModel viewModel;
    private CountDownTimer resendTimer;
    private boolean resendEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        if (email == null) email = "";

        binding.textEmail.setText(email);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        wireDigitBox(binding.digit1, null, binding.digit2);
        wireDigitBox(binding.digit2, binding.digit1, binding.digit3);
        wireDigitBox(binding.digit3, binding.digit2, binding.digit4);
        wireDigitBox(binding.digit4, binding.digit3, null);

        binding.btnVerify.setOnClickListener(v -> submitOtp());

        binding.btnResend.setOnClickListener(v -> {
            if (!resendEnabled) return;
            viewModel.sendOtp();
            startResendCooldown();
            ToastHelper.success(this, "OTP resent to your email.");
        });

        viewModel.getStateLiveData().observe(this, state -> {
            boolean loading = state.getLoading();
            binding.btnVerify.setEnabled(!loading);
            binding.btnVerify.setText(loading ? "Verifying…" : "Verify");
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);

            String error = state.getError();
            if (error != null && !error.isEmpty()) {
                ToastHelper.error(this, error);
            }

            if (state.getOtpVerified()) {
                DataManager.getInstance().loadData(getApplicationContext());
                startActivity(new Intent(this, MainDashboardActivity.class));
                NavAnim.slideForward(this);
                finish();
            }
        });

        startResendCooldown();
    }

    private void submitOtp() {
        String d1 = text(binding.digit1);
        String d2 = text(binding.digit2);
        String d3 = text(binding.digit3);
        String d4 = text(binding.digit4);

        if (d1.isEmpty() || d2.isEmpty() || d3.isEmpty() || d4.isEmpty()) {
            ToastHelper.warning(this, "Please enter all 4 digits.");
            return;
        }

        viewModel.verifyOtp(d1 + d2 + d3 + d4);
    }

    private void wireDigitBox(EditText current, EditText prev, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1 && next != null) {
                    next.requestFocus();
                }
            }
        });

        current.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && keyCode == KeyEvent.KEYCODE_DEL
                    && current.getText().length() == 0
                    && prev != null) {
                prev.requestFocus();
                prev.setText("");
                return true;
            }
            return false;
        });
    }

    private void startResendCooldown() {
        resendEnabled = false;
        binding.btnResend.setEnabled(false);

        if (resendTimer != null) {
            resendTimer.cancel();
        }

        resendTimer = new CountDownTimer(RESEND_COOLDOWN_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                binding.btnResend.setText(String.format(Locale.getDefault(), "Resend OTP (%ds)", seconds));
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                binding.btnResend.setEnabled(true);
                binding.btnResend.setText("Resend OTP");
            }
        }.start();
    }

    private String text(EditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resendTimer != null) {
            resendTimer.cancel();
        }
    }
}
