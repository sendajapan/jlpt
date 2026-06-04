package com.scholarlyapps.pathlingo.ui.activities.profile;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.databinding.ActivityChangePasswordBinding;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.ChangePasswordViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private ChangePasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.toolbar.getLayoutParams();
            params.topMargin = bars.top;
            binding.toolbar.setLayoutParams(params);
            binding.scrollView.setPadding(0, 0, 0, bars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(ChangePasswordViewModel.class);

        binding.toolbar.setNavigationOnClickListener(v -> {
            finish();
            NavAnim.slideBack(this);
        });

        binding.btnSendResetLink.setOnClickListener(v -> submitResetLink());

        viewModel.getUser().observe(this, user -> {
            if (user != null && !user.email.isEmpty() && binding.editEmail.getText().toString().isEmpty()) {
                binding.editEmail.setText(user.email);
            }
        });

        viewModel.getState().observe(this, state -> {
            boolean loading = state.getLoading();
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.btnSendResetLink.setEnabled(!loading);
            binding.btnSendResetLink.setText(loading ? "Sending…" : "Send Reset Link");

            String error = state.getError();
            if (error != null && !error.isEmpty()) {
                ToastHelper.error(this, error);
            }

            if (state.getSuccess()) {
                ToastHelper.successLong(this, "Reset link sent! Check your email.");
                finish();
                NavAnim.slideBack(this);
            }
        });
    }

    private void submitResetLink() {
        String email = binding.editEmail.getText() != null ? binding.editEmail.getText().toString().trim() : "";
        viewModel.sendResetLink(email);
    }

    @Override
    public void onBackPressed() {
        NavAnim.slideBack(this);
        super.onBackPressed();
    }
}
