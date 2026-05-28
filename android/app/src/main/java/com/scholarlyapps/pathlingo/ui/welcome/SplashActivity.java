package com.scholarlyapps.pathlingo.ui.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.ui.activities.MainDashboardActivity;
import com.scholarlyapps.pathlingo.ui.auth.LoginActivity;
import com.scholarlyapps.pathlingo.ui.auth.SplashViewModel;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    public static final long WAIT_DURATION = 3_000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceLocator.init(this);
        setContentView(R.layout.activity_splash);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView())
                .setAppearanceLightStatusBars(true);

        LinearLayout splashContent = findViewById(R.id.splashContent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout), (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            splashContent.setPadding(0, top, 0, bottom);
            return insets;
        });

        SplashViewModel viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        viewModel.getAuthenticated().observe(this, authenticated -> {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                boolean onboardingDone = getSharedPreferences(
                        OnboardingActivity.PREFS_NAME, MODE_PRIVATE)
                        .getBoolean(OnboardingActivity.KEY_ONBOARDING_DONE, false);
                if (!onboardingDone) {
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                    NavAnim.slideForward(SplashActivity.this);
                } else if (authenticated) {
                    goToDashboard();
                    return;
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    NavAnim.slideForward(SplashActivity.this);
                }
                finish();
            }, WAIT_DURATION);
        });

        viewModel.checkAuth();
    }

    private void goToDashboard() {
        DataManager.getInstance().loadData(getApplicationContext());
        startActivity(new Intent(this, MainDashboardActivity.class));
        NavAnim.slideForward(this);
        finish();
    }
}
