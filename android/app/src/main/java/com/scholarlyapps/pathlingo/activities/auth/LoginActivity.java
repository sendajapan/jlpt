package com.scholarlyapps.pathlingo.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.activities.MainDashboardActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button beginBtn = findViewById(R.id.btn_begin_learning);
        beginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainDashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
