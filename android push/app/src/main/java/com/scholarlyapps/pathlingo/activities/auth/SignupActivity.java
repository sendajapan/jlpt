package com.scholarlyapps.pathlingo.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.activities.MainDashboardActivity;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button createBtn = findViewById(R.id.btn_create_account);
        createBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, MainDashboardActivity.class);
            startActivity(intent);
            finish();
        });

        TextView loginLink = findViewById(R.id.tv_login);
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
