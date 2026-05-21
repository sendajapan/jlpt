package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;

import org.json.JSONObject;

public class ScoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        JSONObject lessonData = DataManager.getInstance().getLessonData();
        if (lessonData != null) {
            int score = lessonData.optInt("score", 86);
            int maxScore = lessonData.optInt("maxScore", 100);
            int stars = lessonData.optInt("stars", 3);
            int xpEarned = lessonData.optInt("xpEarned", 85);
            int correctCount = lessonData.optInt("correctCount", 4);
            int totalCount = lessonData.optInt("totalCount", 5);
            String time = lessonData.optString("time", "2:14");

            TextView headingView = findViewById(R.id.heading);
            headingView.setText("すばらしい! / Wonderful!");

            ProgressBar scoreBar = findViewById(R.id.score_progress);
            scoreBar.setProgress(score);

            TextView scoreTextView = findViewById(R.id.score_text);
            scoreTextView.setText(score + "/" + maxScore);

            LinearLayout starsContainer = findViewById(R.id.stars_container);
            starsContainer.removeAllViews();
            for (int i = 0; i < 3; i++) {
                TextView star = new TextView(this);
                star.setText(i < stars ? "⭐" : "☆");
                star.setTextSize(24);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);
                star.setLayoutParams(params);
                starsContainer.addView(star);
            }

            TextView xpView = findViewById(R.id.xp_earned);
            xpView.setText("+" + xpEarned + " XP");

            TextView correctView = findViewById(R.id.correct_count);
            correctView.setText(correctCount + "/" + totalCount + " correct");

            TextView timeView = findViewById(R.id.time);
            timeView.setText(time);

            ProgressBar levelProgress = findViewById(R.id.level_progress);
            levelProgress.setProgress(64);

            TextView levelTextView = findViewById(R.id.level_text);
            levelTextView.setText("320/500 XP");

            TextView toGoView = findViewById(R.id.to_go);
            toGoView.setText("180 to go");
        }

        Button continueBtn = findViewById(R.id.btn_continue);
        continueBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, MainDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
