package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.scholarlyapps.pathlingo.databinding.ActivityKanjiBinding;

public class KanjiActivity extends AppCompatActivity {

    private ActivityKanjiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanjiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.scrollView.setPadding(0, 0, 0, bottom);
            return insets;
        });

        binding.toolbar.setOnBackClickListener(v -> finish());

        binding.cardJlpt.setOnClickListener(v -> startActivity(new Intent(this, KanjiLevelActivity.class)));
        binding.cardStrokes.setOnClickListener(v -> startActivity(new Intent(this, KanjiStrokeActivity.class)));
        binding.cardPath.setOnClickListener(v -> showComingSoon());
        binding.cardCategories.setOnClickListener(v -> showComingSoon());
        binding.cardGrade.setOnClickListener(v -> showComingSoon());
        binding.quizCard.btnStartQuiz.setOnClickListener(v -> startActivity(new Intent(this, QuizActivity.class)));
    }

    private void showComingSoon() {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
    }
}
