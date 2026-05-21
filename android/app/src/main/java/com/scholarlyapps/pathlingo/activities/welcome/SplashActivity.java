package com.scholarlyapps.pathlingo.activities.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.activities.auth.LoginActivity;
import com.scholarlyapps.pathlingo.data.DataManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DataManager.getInstance().loadData(this);

        new Handler(getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 2000);
    }
}
