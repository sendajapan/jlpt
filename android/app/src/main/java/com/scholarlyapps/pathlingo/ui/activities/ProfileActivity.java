package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.User;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = DataManager.getInstance().getUser();

        TextView nameView = findViewById(R.id.user_name);
        nameView.setText(user.name);

        TextView jpNameView = findViewById(R.id.user_jp_name);
        jpNameView.setText(user.jpName);

        TextView levelView = findViewById(R.id.user_level);
        levelView.setText("Level " + user.level);

        TextView subtitleView = findViewById(R.id.user_subtitle);
        subtitleView.setText("Apprentice · since May 2026");

        TextView xpView = findViewById(R.id.user_xp);
        xpView.setText(user.xp + "/" + user.maxXp + " XP");

        ProgressBar xpBar = findViewById(R.id.xp_progress);
        xpBar.setProgress((user.xp * 100) / user.maxXp);

        TextView wordsView = findViewById(R.id.stat_words);
        wordsView.setText(user.wordsKnown + " words");

        TextView streakView = findViewById(R.id.stat_streak);
        streakView.setText(user.streak + " day streak");

        TextView accuracyView = findViewById(R.id.stat_accuracy);
        accuracyView.setText(user.accuracy + "% accuracy");

        // Settings toggles
        Switch notifSwitch = findViewById(R.id.toggle_notifications);
        Switch soundSwitch = findViewById(R.id.toggle_sound);
        Switch darkSwitch = findViewById(R.id.toggle_dark);

        notifSwitch.setChecked(true);
        soundSwitch.setChecked(true);
        darkSwitch.setChecked(false);

        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> finish());
    }
}
