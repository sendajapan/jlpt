package com.scholarlyapps.pathlingo.ui.activities;

import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.databinding.ActivityWordBinding;
import com.scholarlyapps.pathlingo.models.Word;
import com.scholarlyapps.pathlingo.ui.utils.ShimmerImage;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.WordDetailViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class WordActivity extends AppCompatActivity {

    public static final String EXTRA_SUBCATEGORY_ID = "subcategoryId";
    public static final String EXTRA_WORD_INDEX = "wordIndex";

    private ActivityWordBinding binding;
    private Word currentWord;
    private MediaPlayer mediaPlayer;
    private List<String> playAllQueue;
    private int playAllIndex;
    private boolean playingAll;
    private boolean learnedRequestInFlight;

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

        if (word.bgUrl != null && !word.bgUrl.isEmpty()) {
            ShimmerImage.load(binding.bgShimmer, binding.imgWordBg, word.bgUrl);
        } else {
            ShimmerImage.load(binding.bgShimmer, binding.imgWordBg, null);
            binding.imgWordBg.setImageDrawable(new ColorDrawable(
                ContextCompat.getColor(this, R.color.color_theme_extra_light)));
        }

        ShimmerImage.load(binding.imageShimmer, binding.imgWord, word.img);

        updateFavoriteButton(word.favorite);
        updateBookmarkButton(word.bookmarked);
        updateLearnedButton(word.learned);

        bindAudioButton(binding.btnPlayWord, word.audioUrl);
        bindAudioButton(binding.btnPlayRomaji, word.audioUrl);
        bindAudioButton(binding.btnPlayEn, word.audioEnUrl);
        bindAudioButton(binding.btnPlaySentenceJp, word.sentenceAudioJpUrl);
        bindAudioButton(binding.btnPlaySentenceRomaji, word.sentenceAudioJpUrl);
        bindAudioButton(binding.btnPlaySentenceEn, word.sentenceAudioEnUrl);

        binding.btnPlayAll.setOnClickListener(v -> togglePlayAll(word));
        binding.btnFavorite.setOnClickListener(v -> toggleFavorite(word));
        binding.btnBookmark.setOnClickListener(v -> toggleBookmark(word));
        binding.btnLearn.setOnClickListener(v -> markLearned(word));
    }

    private void bindAudioButton(View btn, String url) {
        if (url == null || url.isEmpty()) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(v -> {
                stopPlayAll();
                playAudio(url, null);
            });
        }
    }

    private void updateFavoriteButton(boolean isFavorite) {
        binding.btnFavorite.setText(isFavorite ? "Favorited" : "Favorite");
    }

    private void updateBookmarkButton(boolean isBookmarked) {
        binding.btnBookmark.setText(isBookmarked ? "Bookmarked" : "Bookmark");
    }

    private void updateLearnedButton(boolean isLearned) {
        binding.btnLearn.setEnabled(!isLearned && !learnedRequestInFlight);
        binding.btnLearn.setText(isLearned ? "Learned ✓" : "I have learned this");
    }

    private void toggleFavorite(Word word) {
        word.favorite = !word.favorite;
        updateFavoriteButton(word.favorite);
        ServiceLocator.wordActionRepository.setFavorite(word.id, word.favorite);
    }

    private void toggleBookmark(Word word) {
        word.bookmarked = !word.bookmarked;
        updateBookmarkButton(word.bookmarked);
        ServiceLocator.wordActionRepository.setBookmarked(word.id, word.bookmarked);
    }

    private void markLearned(Word word) {
        if (word.learned || learnedRequestInFlight) return;
        learnedRequestInFlight = true;
        binding.btnLearn.setEnabled(false);
        binding.btnLearn.setText("Saving…");
        ServiceLocator.wordActionRepository.markLearned(word.id, totalCoins ->
            runOnUiThread(() -> {
                if (isFinishing()) return;
                learnedRequestInFlight = false;
                word.learned = true;
                updateLearnedButton(true);
                if (totalCoins != null) {
                    ToastHelper.success(this, "Marked as learned! You now have " + totalCoins + " coins.");
                    Executors.newSingleThreadExecutor().execute(ServiceLocator.userRepository::refresh);
                } else {
                    ToastHelper.success(this, "Marked as learned! Coins will sync when you're back online.");
                }
            })
        );
    }

    private void togglePlayAll(Word word) {
        if (playingAll) {
            stopPlayAll();
            return;
        }
        playAllQueue = new ArrayList<>();
        addIfPresent(playAllQueue, word.audioUrl);
        addIfPresent(playAllQueue, word.audioEnUrl);
        addIfPresent(playAllQueue, word.sentenceAudioJpUrl);
        addIfPresent(playAllQueue, word.sentenceAudioEnUrl);
        if (playAllQueue.isEmpty()) {
            ToastHelper.error(this, "Audio not available");
            return;
        }
        playingAll = true;
        playAllIndex = 0;
        binding.btnPlayAll.setText("Stop");
        playNextInQueue();
    }

    private void addIfPresent(List<String> queue, String url) {
        if (url != null && !url.isEmpty()) queue.add(url);
    }

    private void playNextInQueue() {
        if (!playingAll || playAllIndex >= playAllQueue.size()) {
            stopPlayAll();
            return;
        }
        String url = playAllQueue.get(playAllIndex);
        playAllIndex++;
        playAudio(url, this::playNextInQueue);
    }

    private void stopPlayAll() {
        playingAll = false;
        binding.btnPlayAll.setText("Play All (JP + EN)");
        releasePlayer();
    }

    private void playAudio(String url, Runnable onComplete) {
        if (url == null || url.isEmpty()) {
            ToastHelper.error(this, "Audio not available");
            return;
        }
        releasePlayer();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> {
                if (onComplete != null) onComplete.run();
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                if (onComplete != null) {
                    onComplete.run();
                } else {
                    ToastHelper.error(WordActivity.this, "Could not play audio");
                }
                return true;
            });
        } catch (IOException e) {
            if (onComplete != null) {
                onComplete.run();
            } else {
                ToastHelper.error(this, "Could not play audio");
            }
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playingAll = false;
        releasePlayer();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
