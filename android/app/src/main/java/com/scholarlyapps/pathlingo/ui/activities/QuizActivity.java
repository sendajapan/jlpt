package com.scholarlyapps.pathlingo.ui.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ActivityQuizBinding;
import com.scholarlyapps.pathlingo.databinding.DialogQuizDetailResultBinding;
import com.scholarlyapps.pathlingo.databinding.DialogQuizResultBinding;
import com.scholarlyapps.pathlingo.databinding.LayoutQuizQuestionBinding;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.QuizViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

        viewModel.getQuizLoaded().observe(this, loaded -> {
            if (Boolean.TRUE.equals(loaded)) {
                viewModel.advance();
            }
        });

        viewModel.getError().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

        viewModel.getQuizResult().observe(this, result -> {
            if (result != null) {
                viewModel.clearQuizResult();
                showScoreDialog(result.coinsEarned, result.xpEarned);
            }
        });

        binding.btnContinue.setOnClickListener(v -> onContinueClicked());

        viewModel.loadQuiz();
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
        questionBinding.audioPlayerCard.setVisibility(View.GONE);

        switch (question.type) {
            case TEXT:
                questionBinding.txtQuestionLabel.setText("What does this mean?");
                String jpText = question.wordJp.isEmpty() ? question.wordRomaji : question.wordJp;
                questionBinding.txtWordJp.setText(jpText);
                questionBinding.txtWordRomaji.setText(question.wordRomaji);
                questionBinding.txtWordJp.setVisibility(View.VISIBLE);
                questionBinding.txtWordRomaji.setVisibility(View.VISIBLE);
                break;

            case IMAGE:
                questionBinding.txtQuestionLabel.setText("What do you see in the picture?");
                questionBinding.cardImage.setVisibility(View.VISIBLE);
                Coil.imageLoader(this).enqueue(
                    new ImageRequest.Builder(this)
                        .data(question.imageUrl)
                        .crossfade(true)
                        .target(questionBinding.imgQuestion)
                        .build()
                );
                break;

            case AUDIO:
                questionBinding.txtQuestionLabel.setText("Listen and choose");
                questionBinding.audioPlayerCard.setVisibility(View.VISIBLE);
                questionBinding.audioPlayerCard.setOnClickListener(v -> playAudio(question.audioJpUrl));
                playAudio(question.audioJpUrl);
                break;
        }

        optionBtns = List.of(
            questionBinding.btnOpt0,
            questionBinding.btnOpt1,
            questionBinding.btnOpt2,
            questionBinding.btnOpt3
        );

        correctAnswer = question.correctAnswer;

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
                btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_theme_light));
                btn.setTextColor(ContextCompat.getColor(this, R.color.ink));
                btn.setStrokeColorResource(R.color.color_theme_navy);
            } else if (label.equals(text) && !isCorrect) {
                btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.color_streak_missed_border));
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
            viewModel.submitResult();
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
            binding.motionToast.setCardBackgroundColor(
                ContextCompat.getColor(this, R.color.color_level_n1_bg));
            binding.toastLine.setBackgroundColor(
                ContextCompat.getColor(this, R.color.color_error));
            binding.toastIcon.setText("✗");
            binding.toastIcon.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            binding.toastTitle.setText("Incorrect");
            binding.toastTitle.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            binding.toastSubtitle.setText("Answer: " + correctAnswer);
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

    private void showScoreDialog(int coinsEarned, int xpEarned) {
        DialogQuizResultBinding dialogBinding = DialogQuizResultBinding.inflate(LayoutInflater.from(this));
        int score = viewModel.getScore();
        int total = viewModel.getTotal();

        dialogBinding.txtResultScore.setText(score + " / " + total);
        dialogBinding.txtResultCoins.setText("+" + coinsEarned + " coins earned");
        dialogBinding.txtResultXp.setText("+" + xpEarned + " XP earned");

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(dialogBinding.getRoot())
            .setCancelable(false)
            .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogBinding.btnViewResult.setOnClickListener(v -> {
            dialog.dismiss();
            showDetailResultDialog(coinsEarned, xpEarned);
        });

        dialogBinding.btnTryAgain.setOnClickListener(v -> {
            dialog.dismiss();
            viewModel.loadQuiz();
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private void showDetailResultDialog(int coinsEarned, int xpEarned) {
        DialogQuizDetailResultBinding b = DialogQuizDetailResultBinding.inflate(LayoutInflater.from(this));

        int score = viewModel.getScore();
        int total = viewModel.getTotal();
        int accuracy = total > 0 ? (score * 100) / total : 0;
        boolean passed = accuracy >= 60;
        List<Boolean> answerResults = viewModel.getAnswerResults();
        long timeTakenMillis = viewModel.getTimeTakenMillis();

        b.txtDetailBadge.setVisibility(View.VISIBLE);
        if (passed) {
            b.txtDetailTitle.setText("Well Done!");
            b.txtDetailSubtitle.setText("You have passed the quiz");
            b.txtDetailBadge.setText("PASSED");
            b.txtDetailBadge.setTextColor(ContextCompat.getColor(this, R.color.color_level_n5));
            b.txtDetailBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_quiz_pass_badge));
        } else {
            b.txtDetailTitle.setText("Keep Going!");
            b.txtDetailSubtitle.setText("Better luck next time");
            b.txtDetailBadge.setText("FAILED");
            b.txtDetailBadge.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            b.txtDetailBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_quiz_fail_badge));
        }

        b.txtDetailScore.setText(score + " / " + total);
        b.txtDetailXpAmount.setText("+" + xpEarned);
        b.txtDetailCoinsAmount.setText("+" + coinsEarned);
        b.txtCorrectCount.setText(String.valueOf(score));
        b.txtWrongCount.setText(String.valueOf(total - score));
        b.txtScorePercent.setText(accuracy + "%");
        b.progressScore.setProgress(accuracy);
        b.txtDetailTime.setText(formatTimeTaken(timeTakenMillis));
        b.txtDetailAccuracy.setText(accuracy + "%");

        for (int i = 0; i < answerResults.size(); i++) {
            b.questionBallsContainer.addView(createQuestionBall(i + 1, answerResults.get(i)));
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(b.getRoot())
            .setCancelable(false)
            .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        b.btnDetailClose.setOnClickListener(v -> {
            dialog.dismiss();
            Intent result = new Intent();
            result.putExtra(EXTRA_SCORE, score);
            result.putExtra(EXTRA_TOTAL, total);
            setResult(RESULT_OK, result);
            finish();
        });

        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private View createQuestionBall(int number, boolean isCorrect) {
        int size = dpToPx(24);
        int margin = dpToPx(2);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(margin, 0, margin, 0);

        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setText(String.valueOf(number));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        tv.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 10);
        tv.setBackground(ContextCompat.getDrawable(this,
            isCorrect ? R.drawable.bg_circle_correct : R.drawable.bg_circle_wrong));
        return tv;
    }

    private String formatTimeTaken(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void playAudio(String url) {
        if (url == null || url.isEmpty()) return;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (questionBinding != null) {
            questionBinding.audioPlayerCard.setAlpha(0.6f);
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                if (questionBinding != null) {
                    questionBinding.audioPlayerCard.setAlpha(1f);
                }
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                if (questionBinding != null) {
                    questionBinding.audioPlayerCard.setAlpha(1f);
                }
            });
        } catch (IOException e) {
            if (questionBinding != null) {
                questionBinding.audioPlayerCard.setAlpha(1f);
            }
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
