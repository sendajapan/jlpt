package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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
import com.scholarlyapps.pathlingo.data.remote.dto.KanaDto;
import com.scholarlyapps.pathlingo.databinding.ActivityKanaDetailBinding;
import com.scholarlyapps.pathlingo.ui.adapters.KanaWordAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.KanaDetailViewModel;

import java.util.Locale;

public class KanaDetailActivity extends AppCompatActivity {

    public static final String EXTRA_KANA_ID = "kanaId";

    private ActivityKanaDetailBinding binding;
    private KanaDetailViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private long kanaId;
    private String soundUrl;

    public static Intent intent(Context context, long kanaId) {
        return new Intent(context, KanaDetailActivity.class)
                .putExtra(EXTRA_KANA_ID, kanaId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanaDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.scrollView.setPadding(0, 0, 0, bottom);
            return insets;
        });

        kanaId = getIntent().getLongExtra(EXTRA_KANA_ID, -1);
        if (kanaId == -1) {
            finish();
            return;
        }

        binding.toolbar.setOnBackClickListener(v -> finish());
        binding.rvExampleWords.setLayoutManager(new LinearLayoutManager(this));

        binding.btnPlay.setOnClickListener(v -> playSound());
        binding.btnLearned.setOnClickListener(v -> viewModel.markLearned(kanaId));

        setupDrawingButtons();

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(KanaDetailViewModel.class);

        viewModel.getKana().observe(this, this::showKana);

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

        viewModel.load(kanaId);
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

    private void showKana(KanaDto kana) {
        if (kana == null) return;

        binding.txtKana.setText(kana.kana);
        binding.txtRomaji.setText(kana.romaji != null ? kana.romaji.trim().toUpperCase(Locale.ROOT) : "");
        binding.drawingView.setHint(kana.kana);
        soundUrl = kana.soundUrl;

        if (kana.exampleWords != null && !kana.exampleWords.isEmpty()) {
            binding.cardExampleWords.setVisibility(View.VISIBLE);
            binding.rvExampleWords.setAdapter(new KanaWordAdapter(kana.exampleWords));
        } else {
            binding.cardExampleWords.setVisibility(View.GONE);
        }

        if (kana.isLearned) {
            binding.btnLearned.setText("Learned ✓");
            binding.btnLearned.setEnabled(false);
            binding.btnLearned.setBackgroundTintList(getColorStateList(R.color.color_level_n5));
        }
    }

    private void playSound() {
        if (soundUrl == null || soundUrl.isEmpty()) {
            Toast.makeText(this, "Audio coming soon", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(soundUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        } catch (Exception e) {
            Toast.makeText(this, "Unable to play audio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
