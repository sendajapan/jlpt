package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.models.Word;
import java.util.List;

public class WordDetailActivity extends AppCompatActivity {
    private Word currentWord;
    private List<Word> words;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        String categoryId = getIntent().getStringExtra("categoryId");
        String subcategoryId = getIntent().getStringExtra("subcategoryId");
        currentIndex = getIntent().getIntExtra("wordIndex", 0);

        Category cat = DataManager.getInstance().getCategoryById(categoryId);
        if (cat != null) {
            for (Subcategory subcat : cat.subcategories) {
                if (subcat.id.equals(subcategoryId)) {
                    words = subcat.words;
                    if (currentIndex < words.size()) {
                        currentWord = words.get(currentIndex);
                        displayWord();
                    }
                    break;
                }
            }
        }

        Button nextBtn = findViewById(R.id.btn_next);
        nextBtn.setOnClickListener(v -> {
            if (currentIndex < words.size() - 1) {
                currentIndex++;
                currentWord = words.get(currentIndex);
                displayWord();
            } else {
                Intent intent = new Intent(WordDetailActivity.this, ScoreActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> finish());
    }

    private void displayWord() {
        TextView kanjiView = findViewById(R.id.kanji);
        kanjiView.setText(currentWord.kanji);

        TextView readingView = findViewById(R.id.reading);
        readingView.setText(currentWord.reading);

        TextView romajiView = findViewById(R.id.romaji);
        romajiView.setText(currentWord.romaji);

        TextView enView = findViewById(R.id.english);
        enView.setText(currentWord.en);

        TextView exampleJpView = findViewById(R.id.example_jp);
        exampleJpView.setText(currentWord.example_jp);

        TextView exampleRomajiView = findViewById(R.id.example_romaji);
        exampleRomajiView.setText(currentWord.example_romaji);

        TextView exampleEnView = findViewById(R.id.example_en);
        exampleEnView.setText(currentWord.example_en);

        LinearLayout chipsContainer = findViewById(R.id.chips_container);
        chipsContainer.removeAllViews();

        String[] chips = {currentWord.type, currentWord.jlpt, "+" + currentWord.xp + " XP"};
        for (String chip : chips) {
            TextView chipView = new TextView(this);
            chipView.setText(chip);
            chipView.setTextSize(11);
            chipView.setTextColor(getColor(R.color.white));
            chipView.setBackgroundResource(R.drawable.bg_pill_sage);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 8, 0);
            chipView.setLayoutParams(params);
            chipView.setPadding(12, 8, 12, 8);
            chipsContainer.addView(chipView);
        }
    }
}
