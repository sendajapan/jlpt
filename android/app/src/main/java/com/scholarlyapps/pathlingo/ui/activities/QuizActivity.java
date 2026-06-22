package com.scholarlyapps.pathlingo.ui.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ActivityQuizBinding;
import com.scholarlyapps.pathlingo.databinding.DialogQuizResultBinding;
import com.scholarlyapps.pathlingo.databinding.LayoutQuizQuestionBinding;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.QuizViewModel;

import java.io.IOException;
import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "score";
    public static final String EXTRA_TOTAL = "total";

    private ActivityQuizBinding binding;
    private LayoutQuizQuestionBinding questionBinding;
    private QuizViewModel viewModel;
    private MediaPlayer mediaPlayer;

    private List<MaterialButton> optionBtns;
    private String selectedAnswer = null;
    private String correctAnswer = null;
    private QuizViewModel.QuizQuestion currentQuestion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.btnContinue.setPadding(
                binding.btnContinue.getPaddingLeft(),
                binding.btnContinue.getPaddingTop(),
                binding.btnContinue.getPaddingRight(),
                binding.btnContinue.getPaddingBottom() + bottom
            );
            return insets;
        });

        binding.toolbar.setOnBackClickListener(v -> finish());

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(QuizViewModel.class);

        viewModel.getWordsLoaded().observe(this, loaded -> {
            if (Boolean.TRUE.equals(loaded)) {
                viewModel.advance();
            }
        });

        viewModel.getCurrentQuestion().observe(this, question -> {
            if (question == null) return;
            currentQuestion = question;
            selectedAnswer = null;
            hideToast();
            resetContinue();
            updateProgress();
            showQuestion(question);
        });

        binding.btnContinue.setOnClickListener(v -> onContinueClicked());

        viewModel.loadWords();
    }

    private void updateProgress() {
        int display = viewModel.getCurrentIndex() + 1;
        int total = viewModel.getTotal();
        binding.txtProgress.setText(display + " / " + total);
        binding.progressBar.setProgress(display);
    }

    private void showQuestion(QuizViewModel.QuizQuestion question) {
        questionBinding = LayoutQuizQuestionBinding.inflate(getLayoutInflater());
        binding.questionContainer.removeAllViews();
        binding.questionContainer.addView(questionBinding.getRoot());

        questionBinding.txtWordJp.setVisibility(View.GONE);
        questionBinding.txtWordRomaji.setVisibility(View.GONE);
        questionBinding.cardImage.setVisibility(View.GONE);
        questionBinding.btnPlayAudio.setVisibility(View.GONE);

        switch (question.type) {
            case TEXT:
                questionBinding.txtQuestionLabel.setText("What does this mean?");
                questionBinding.txtWordJp.setText(
                    question.correct.kanji.isEmpty() ? question.correct.romaji : question.correct.kanji);
                questionBinding.txtWordRomaji.setText(question.correct.romaji);
                questionBinding.txtWordJp.setVisibility(View.VISIBLE);
                questionBinding.txtWordRomaji.setVisibility(View.VISIBLE);
                break;

            case IMAGE:
                questionBinding.txtQuestionLabel.setText("What do you see in the picture?");
                questionBinding.cardImage.setVisibility(View.VISIBLE);
                Coil.imageLoader(this).enqueue(
                    new ImageRequest.Builder(this)
                        .data(question.correct.img)
                        .crossfade(true)
                        .target(questionBinding.imgQuestion)
                        .build()
                );
                break;

            case AUDIO:
                questionBinding.txtQuestionLabel.setText("Listen and choose");
                questionBinding.btnPlayAudio.setVisibility(View.VISIBLE);
                questionBinding.btnPlayAudio.setOnClickListener(v -> playAudio(question.correct.audioUrl));
                playAudio(question.correct.audioUrl);
                break;
        }

        optionBtns = List.of(
            questionBinding.btnOpt0,
            questionBinding.btnOpt1,
            questionBinding.btnOpt2,
            questionBinding.btnOpt3
        );

        correctAnswer = question.type == QuizViewModel.QuestionType.TEXT
            ? question.correct.en
            : (question.correct.kanji.isEmpty() ? question.correct.romaji : question.correct.kanji);

        List<String> options = question.options;
        for (int i = 0; i < optionBtns.size(); i++) {
            MaterialButton btn = optionBtns.get(i);
            if (i < options.size()) {
                btn.setText(options.get(i));
                btn.setVisibility(View.VISIBLE);
                final String text = options.get(i);
                btn.setOnClickListener(v -> onOptionTapped(text));
            } else {
                btn.setVisibility(View.GONE);
            }
        }
    }

    private void onOptionTapped(String text) {
        if (selectedAnswer != null) return;
        selectedAnswer = text;

        boolean isCorrect = text.equals(correctAnswer);
        viewModel.recordAnswer(isCorrect);

        for (MaterialButton btn : optionBtns) {
            btn.setEnabled(false);
            String label = btn.getText().toString();
            if (label.equals(correctAnswer)) {
                btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_level_n5));
                btn.setTextColor(ContextCompat.getColor(this, R.color.white));
                btn.setStrokeColorResource(R.color.color_level_n5);
            } else if (label.equals(text) && !isCorrect) {
                btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_error));
                btn.setTextColor(ContextCompat.getColor(this, R.color.white));
                btn.setStrokeColorResource(R.color.color_error);
            }
        }

        showMotionToast(isCorrect);
        enableContinue();
    }

    private void onContinueClicked() {
        if (selectedAnswer == null) return;
        hideToast();
        if (viewModel.isFinished()) {
            showScoreDialog();
        } else {
            viewModel.advance();
        }
    }

    private void showMotionToast(boolean isCorrect) {
        if (isCorrect) {
            binding.motionToast.setCardBackgroundColor(
                ContextCompat.getColor(this, R.color.color_level_n5_bg));
            binding.toastLine.setBackgroundColor(
                ContextCompat.getColor(this, R.color.color_level_n5));
            binding.toastIcon.setText("✓");
            binding.toastIcon.setTextColor(ContextCompat.getColor(this, R.color.color_level_n5));
            binding.toastTitle.setText("Correct!");
            binding.toastTitle.setTextColor(ContextCompat.getColor(this, R.color.color_level_n5));
            binding.toastSubtitle.setText("Right answer");
            binding.toastSubtitle.setTextColor(ContextCompat.getColor(this, R.color.color_level_n5));
        } else {
            String hint = currentQuestion.type == QuizViewModel.QuestionType.TEXT
                ? currentQuestion.correct.en
                : (currentQuestion.correct.kanji.isEmpty()
                    ? currentQuestion.correct.romaji : currentQuestion.correct.kanji);
            binding.motionToast.setCardBackgroundColor(
                ContextCompat.getColor(this, R.color.color_level_n1_bg));
            binding.toastLine.setBackgroundColor(
                ContextCompat.getColor(this, R.color.color_error));
            binding.toastIcon.setText("✗");
            binding.toastIcon.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            binding.toastTitle.setText("Incorrect");
            binding.toastTitle.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            binding.toastSubtitle.setText("Answer: " + hint);
            binding.toastSubtitle.setTextColor(ContextCompat.getColor(this, R.color.color_error));
        }

        binding.motionToast.setVisibility(View.VISIBLE);
        binding.motionToast.setAlpha(0f);
        binding.motionToast.setTranslationY(binding.motionToast.getHeight() + 40f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
            ObjectAnimator.ofFloat(binding.motionToast, "alpha", 0f, 1f),
            ObjectAnimator.ofFloat(binding.motionToast, "translationY",
                binding.motionToast.getTranslationY(), 0f)
        );
        set.setDuration(280);
        set.start();
    }

    private void hideToast() {
        binding.motionToast.setVisibility(View.GONE);
    }

    private void enableContinue() {
        binding.btnContinue.setEnabled(true);
        binding.btnContinue.setBackgroundTintList(
            ContextCompat.getColorStateList(this, R.color.color_theme_light));
    }

    private void resetContinue() {
        binding.btnContinue.setEnabled(false);
        binding.btnContinue.setBackgroundTintList(
            ContextCompat.getColorStateList(this, R.color.color_theme_extra_light));
    }

    private void showScoreDialog() {
        DialogQuizResultBinding dialogBinding = DialogQuizResultBinding.inflate(LayoutInflater.from(this));
        int score = viewModel.getScore();
        int total = viewModel.getTotal();
        int coins = score * 10;

        dialogBinding.txtResultScore.setText(score + " / " + total);
        dialogBinding.txtResultCoins.setText("+" + coins + " coins earned");

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(dialogBinding.getRoot())
            .setCancelable(false)
            .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogBinding.btnViewResult.setOnClickListener(v -> {
            dialog.dismiss();
            Intent result = new Intent();
            result.putExtra(EXTRA_SCORE, score);
            result.putExtra(EXTRA_TOTAL, total);
            setResult(RESULT_OK, result);
            finish();
        });

        dialogBinding.btnTryAgain.setOnClickListener(v -> {
            dialog.dismiss();
            viewModel.loadWords();
        });

        dialog.show();
    }

    private void playAudio(String url) {
        if (url == null || url.isEmpty()) return;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        } catch (IOException e) {
            // audio not available — silent fail
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
