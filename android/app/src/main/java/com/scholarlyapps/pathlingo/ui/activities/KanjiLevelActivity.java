package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ActivityKanjiLevelBinding;
import com.scholarlyapps.pathlingo.ui.adapters.KanjiLevelAdapter;

import java.util.Arrays;
import java.util.List;

public class KanjiLevelActivity extends AppCompatActivity {

    private static final List<KanjiLevelAdapter.Level> LEVELS = Arrays.asList(
            new KanjiLevelAdapter.Level("N5", "First Steps in Japanese", R.color.color_level_n5, R.color.color_level_n5_bg),
            new KanjiLevelAdapter.Level("N4", "Building Confidence", R.color.color_level_n4, R.color.color_level_n4_bg),
            new KanjiLevelAdapter.Level("N3", "Express Yourself Naturally", R.color.color_level_n3, R.color.color_level_n3_bg),
            new KanjiLevelAdapter.Level("N2", "Understand Complex Japanese", R.color.color_level_n2, R.color.color_level_n2_bg),
            new KanjiLevelAdapter.Level("N1", "Master Japanese Fluency", R.color.color_level_n1, R.color.color_level_n1_bg)
    );

    private ActivityKanjiLevelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanjiLevelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.rvLevels.setPadding(binding.rvLevels.getPaddingLeft(), 0, binding.rvLevels.getPaddingRight(), bottom);
            return insets;
        });

        binding.toolbar.setOnBackClickListener(v -> finish());

        binding.rvLevels.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLevels.setAdapter(new KanjiLevelAdapter(LEVELS, level ->
                startActivity(KanjiGridActivity.intentForJlpt(this, level))));
    }
}
