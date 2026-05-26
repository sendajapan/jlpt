package com.scholarlyapps.pathlingo.ui.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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
        ServiceLocator.INSTANCE.init(this);
        setContentView(R.layout.activity_splash);

        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        SplashViewModel viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        viewModel.getAuthenticated().observe(this, authenticated -> {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (authenticated) {
                    goToDashboard();
                } else {
                    boolean onboardingDone = getSharedPreferences(
                            OnboardingActivity.PREFS_NAME, MODE_PRIVATE)
                            .getBoolean(OnboardingActivity.KEY_ONBOARDING_DONE, false);
                    //if (!onboardingDone) {
                        startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
//                    } else {
//                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    }
                    NavAnim.slideForward(SplashActivity.this);
                    finish();
                }
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
