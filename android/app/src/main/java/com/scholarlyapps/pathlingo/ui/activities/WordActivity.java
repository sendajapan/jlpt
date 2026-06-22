package com.scholarlyapps.pathlingo.ui.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.databinding.ActivityWordBinding;
import com.scholarlyapps.pathlingo.models.Word;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.WordDetailViewModel;

import java.io.IOException;
import java.util.concurrent.Executors;

import coil.Coil;
import coil.request.ImageRequest;

public class WordActivity extends AppCompatActivity {

    public static final String EXTRA_SUBCATEGORY_ID = "subcategoryId";
    public static final String EXTRA_WORD_INDEX = "wordIndex";

    private ActivityWordBinding binding;
    private Word currentWord;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.scrollView.setPadding(
                binding.scrollView.getPaddingLeft(),
                binding.scrollView.getPaddingTop(),
                binding.scrollView.getPaddingRight(),
                bottom
            );
            return insets;
        });

        String subcategoryId = getIntent().getStringExtra(EXTRA_SUBCATEGORY_ID);
        int wordIndex = getIntent().getIntExtra(EXTRA_WORD_INDEX, 0);

        if (subcategoryId == null) {
            finish();
            return;
        }

        binding.toolbar.setOnBackClickListener(v -> finish());

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        WordDetailViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory())
                .get(WordDetailViewModel.class);

        viewModel.getWords().observe(this, words -> {
            if (words == null || wordIndex >= words.size()) return;
            currentWord = words.get(wordIndex);
            bindWord(currentWord);
        });

        viewModel.load(Long.parseLong(subcategoryId));
    }

    private void switchTab(int position) {
        binding.contentWord.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        binding.contentSentence.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
        binding.contentStory.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
        binding.scrollView.scrollTo(0, 0);
    }

    private void bindWord(Word word) {
        binding.txtKanji.setText(word.kanji);
        binding.txtReading.setText(word.reading);
        binding.txtRomaji.setText(word.romaji);
        binding.txtEn.setText(word.en);
        binding.txtExampleJp.setText(word.example_jp);
        binding.txtExampleRomaji.setText(word.example_romaji);
        binding.txtExampleEn.setText(word.example_en);

        if (word.img != null && !word.img.isEmpty()) {
            Coil.imageLoader(this).enqueue(
                new ImageRequest.Builder(this)
                    .data(word.img)
                    .crossfade(true)
                    .target(binding.imgWord)
                    .build()
            );
        }

        updateFavoriteButton(word.favorite);

        binding.btnPlayWord.setOnClickListener(v -> playAudio(word.audioUrl));
        binding.btnPlayRomaji.setOnClickListener(v -> playAudio(word.audioUrl));
        binding.btnPlayEn.setOnClickListener(v -> playAudio(word.audioEnUrl));
        binding.btnPlaySentenceJp.setOnClickListener(v -> playAudio(word.sentenceAudioJpUrl));
        binding.btnPlaySentenceRomaji.setOnClickListener(v -> playAudio(word.sentenceAudioJpUrl));
        binding.btnPlaySentenceEn.setOnClickListener(v -> playAudio(word.sentenceAudioEnUrl));

        binding.btnFavorite.setOnClickListener(v -> toggleFavorite(word));
        binding.btnBookmark.setOnClickListener(v ->
            Toast.makeText(this, "Bookmark saved!", Toast.LENGTH_SHORT).show()
        );

        binding.btnLearn.setOnClickListener(v ->
            Toast.makeText(this, "+100 coins earned!", Toast.LENGTH_SHORT).show()
        );
    }

    private void updateFavoriteButton(boolean isFavorite) {
        binding.btnFavorite.setText(isFavorite ? "Favorited" : "Favorite");
    }

    private void toggleFavorite(Word word) {
        word.favorite = !word.favorite;
        updateFavoriteButton(word.favorite);
        Executors.newSingleThreadExecutor().execute(() -> {
            if (word.favorite) {
                ServiceLocator.categoryRepository.addFavorite(word.id);
            } else {
                ServiceLocator.categoryRepository.removeFavorite(word.id);
            }
        });
    }

    private void playAudio(String url) {
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "Audio not available", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(WordActivity.this, "Could not play audio", Toast.LENGTH_SHORT).show();
                return true;
            });
        } catch (IOException e) {
            Toast.makeText(this, "Could not play audio", Toast.LENGTH_SHORT).show();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
