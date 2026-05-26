package com.scholarlyapps.pathlingo.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.adapters.OnboardingPagerAdapter;
import com.scholarlyapps.pathlingo.ui.auth.LoginActivity;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.viewmodels.OnboardingViewModel;

public class OnboardingActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "pathlingo_prefs";
    public static final String KEY_ONBOARDING_DONE = "onboarding_complete";

    private ViewPager2 viewPager;
    private TextView txtStepIndicator;
    private MaterialButton btnContinue;
    private MaterialButton btnBack;
    private View stepBar1, stepBar2, stepBar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        new ViewModelProvider(this).get(OnboardingViewModel.class);

        txtStepIndicator = findViewById(R.id.txtStepIndicator);
        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.btnBack);
        stepBar1 = findViewById(R.id.stepBar1);
        stepBar2 = findViewById(R.id.stepBar2);
        stepBar3 = findViewById(R.id.stepBar3);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new OnboardingPagerAdapter(this));
        viewPager.setUserInputEnabled(false);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateStepUI(position);
            }
        });

        btnContinue.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < 2) {
                viewPager.setCurrentItem(current + 1);
            } else {
                finishOnboarding();
            }
        });

        btnBack.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current > 0) {
                viewPager.setCurrentItem(current - 1);
            }
        });

        findViewById(R.id.btnSkip).setOnClickListener(v -> finishOnboarding());
    }

    @Override
    public void onBackPressed() {
        int current = viewPager.getCurrentItem();
        if (current > 0) {
            viewPager.setCurrentItem(current - 1);
        } else {
            super.onBackPressed();
        }
    }

    private void updateStepUI(int page) {
        txtStepIndicator.setText("Step " + (page + 1) + " of 3");

        stepBar1.setBackgroundResource(R.drawable.bg_step_bar_active);
        stepBar2.setBackgroundResource(page >= 1 ? R.drawable.bg_step_bar_active : R.drawable.bg_step_bar_inactive);
        stepBar3.setBackgroundResource(page >= 2 ? R.drawable.bg_step_bar_active : R.drawable.bg_step_bar_inactive);

        btnContinue.setText(page == 2 ? "Start Learning" : "Continue");
        btnBack.setVisibility(page == 0 ? View.GONE : View.VISIBLE);
    }

    private void finishOnboarding() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_ONBOARDING_DONE, true)
                .apply();
        startActivity(new Intent(this, LoginActivity.class));
        NavAnim.slideForward(this);
        finish();
    }
}
