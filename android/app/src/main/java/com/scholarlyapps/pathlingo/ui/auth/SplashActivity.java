package com.scholarlyapps.pathlingo.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.ui.activities.MainDashboardActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceLocator.INSTANCE.init(this);
        setContentView(R.layout.activity_splash);

        SplashViewModel viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        viewModel.getAuthenticated().observe(this, authenticated -> {
            if (Boolean.TRUE.equals(authenticated)) {
                goToDashboard();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
        viewModel.checkAuth();
    }

    private void goToDashboard() {
        DataManager.getInstance().loadData(getApplicationContext());
        startActivity(new Intent(this, MainDashboardActivity.class));
        finish();
    }
}
