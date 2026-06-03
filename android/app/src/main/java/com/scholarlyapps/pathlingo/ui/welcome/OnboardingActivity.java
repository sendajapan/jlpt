package com.scholarlyapps.pathlingo.ui.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.adapters.OnboardingPagerAdapter;
import com.scholarlyapps.pathlingo.databinding.ActivityOnboardingBinding;
import com.scholarlyapps.pathlingo.ui.activities.MainDashboardActivity;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.viewmodels.OnboardingViewModel;

public class OnboardingActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "pathlingo_prefs";
    public static final String KEY_ONBOARDING_DONE = "onboarding_complete";

    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.WHITE);
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        insetsController.setAppearanceLightStatusBars(true);
        insetsController.setAppearanceLightNavigationBars(true);

        new ViewModelProvider(this).get(OnboardingViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int navBarBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            binding.btnContainer.setPadding(0, 0, 0, navBarBottom);
            return insets;
        });

        binding.viewPager.setAdapter(new OnboardingPagerAdapter(this));
        binding.viewPager.setUserInputEnabled(false);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateStepUI(position);
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current < 2) {
                binding.viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        binding.btnBack.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current > 0) {
                binding.viewPager.setCurrentItem(current - 1);
            }
        });

        binding.btnSkip.setOnClickListener(v -> finishOnboarding());
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        int current = binding.viewPager.getCurrentItem();
        if (current > 0) {
            binding.viewPager.setCurrentItem(current - 1);
        } else {
            super.onBackPressed();
        }
    }

    private void updateStepUI(int page) {
        binding.txtStepIndicator.setText("Step " + (page + 1) + " of 3");

        binding.stepBar1.setBackgroundResource(R.drawable.bg_step_bar_active);
        binding.stepBar2.setBackgroundResource(page >= 1 ? R.drawable.bg_step_bar_active : R.drawable.bg_step_bar_inactive);
        binding.stepBar3.setBackgroundResource(page >= 2 ? R.drawable.bg_step_bar_active : R.drawable.bg_step_bar_inactive);

        binding.btnContinue.setText(page == 2 ? "Start Learning" : "Continue");
        binding.btnBack.setVisibility(page == 0 ? View.GONE : View.VISIBLE);
    }

    private void finishOnboarding() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_ONBOARDING_DONE, true)
                .apply();
        startActivity(new Intent(this, MainDashboardActivity.class));
        NavAnim.slideForward(this);
        finish();
    }
}
