package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.dto.KanjiDto;
import com.scholarlyapps.pathlingo.databinding.ActivityKanjiDetailBinding;
import com.scholarlyapps.pathlingo.ui.adapters.KanaWordAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.KanjiDetailViewModel;

public class KanjiDetailActivity extends AppCompatActivity {

    public static final String EXTRA_KANJI_ID = "kanjiId";

    private ActivityKanjiDetailBinding binding;
    private KanjiDetailViewModel viewModel;
    private long kanjiId;

    public static Intent intent(Context context, long kanjiId) {
        return new Intent(context, KanjiDetailActivity.class)
                .putExtra(EXTRA_KANJI_ID, kanjiId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanjiDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.scrollView.setPadding(0, 0, 0, bottom);
            return insets;
        });

        kanjiId = getIntent().getLongExtra(EXTRA_KANJI_ID, -1);
        if (kanjiId == -1) {
            finish();
            return;
        }

        binding.toolbar.setOnBackClickListener(v -> finish());
        binding.rvExampleWords.setLayoutManager(new LinearLayoutManager(this));

        binding.btnLearned.setOnClickListener(v -> viewModel.markLearned(kanjiId));

        setupDrawingButtons();

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(KanjiDetailViewModel.class);

        viewModel.getKanji().observe(this, this::showKanji);

        viewModel.getSaving().observe(this, saving ->
                binding.btnLearned.setEnabled(!Boolean.TRUE.equals(saving)));

        viewModel.getLearnResult().observe(this, result -> {
            if (result == null) return;
            if (result.firstTime) {
                Toast.makeText(this, "Great job! +10 coins earned", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getError().observe(this, message -> {
            if (message == null) return;
            viewModel.clearError();
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
        });

        viewModel.load(kanjiId);
    }

    private void setupDrawingButtons() {
        binding.btnPen.setOnClickListener(v -> {
            binding.drawingView.setEraserMode(false);
            highlightTool();
        });
        binding.btnEraser.setOnClickListener(v -> {
            binding.drawingView.setEraserMode(true);
            highlightTool();
        });
        binding.btnUndo.setOnClickListener(v -> binding.drawingView.undo());
        binding.btnRedo.setOnClickListener(v -> binding.drawingView.redo());
        highlightTool();
    }

    private void highlightTool() {
        boolean eraser = binding.drawingView.isEraserMode();
        binding.btnPen.setBackgroundTintList(getColorStateList(eraser ? R.color.white : R.color.color_theme_extra_light));
        binding.btnEraser.setBackgroundTintList(getColorStateList(eraser ? R.color.color_theme_extra_light : R.color.white));
    }

    private void showKanji(KanjiDto kanji) {
        if (kanji == null) return;

        binding.txtKanji.setText(kanji.kanji);
        binding.txtTranslate.setText(kanji.translate != null ? kanji.translate : "");
        binding.txtMeanings.setText(kanji.meanings != null ? kanji.meanings : "");
        binding.txtMeanings.setVisibility(hasText(kanji.meanings) ? View.VISIBLE : View.GONE);
        binding.txtInfo.setText(buildInfo(kanji));
        binding.drawingView.setHint(kanji.kanji);

        showReadings(kanji);

        if (kanji.exampleWords != null && !kanji.exampleWords.isEmpty()) {
            binding.cardExampleWords.setVisibility(View.VISIBLE);
            binding.rvExampleWords.setAdapter(new KanaWordAdapter(kanji.exampleWords));
        } else {
            binding.cardExampleWords.setVisibility(View.GONE);
        }

        if (kanji.isLearned) {
            binding.btnLearned.setText("Learned ✓");
            binding.btnLearned.setEnabled(false);
            binding.btnLearned.setBackgroundTintList(getColorStateList(R.color.color_level_n5));
        }
    }

    private void showReadings(KanjiDto kanji) {
        boolean hasOn = hasText(kanji.readingsOn);
        boolean hasKun = hasText(kanji.readingsKun);

        binding.txtReadingsOn.setText(hasOn ? kanji.readingsOn : "");
        binding.txtReadingsKun.setText(hasKun ? kanji.readingsKun : "");
        binding.rowReadingsOn.setVisibility(hasOn ? View.VISIBLE : View.GONE);
        binding.rowReadingsKun.setVisibility(hasKun ? View.VISIBLE : View.GONE);
        binding.cardReadings.setVisibility(hasOn || hasKun ? View.VISIBLE : View.GONE);
    }

    private String buildInfo(KanjiDto kanji) {
        StringBuilder info = new StringBuilder();
        if (hasText(kanji.jlpt)) {
            info.append("JLPT ").append(kanji.jlpt);
        }
        if (kanji.strokes > 0) {
            if (info.length() > 0) info.append(" • ");
            info.append(kanji.strokes).append(kanji.strokes == 1 ? " stroke" : " strokes");
        }
        return info.toString();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
