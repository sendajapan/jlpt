package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.databinding.ActivityWordDetailBinding;
import com.scholarlyapps.pathlingo.models.Word;
import com.scholarlyapps.pathlingo.ui.UnlockBottomSheet;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.WordDetailViewModel;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SUBCATEGORY_ID = "subcategoryId";
    public static final String EXTRA_ICON_URL = "iconUrl";
    public static final String EXTRA_WORD_INDEX = "wordIndex";

    private ActivityWordDetailBinding binding;
    private List<Word> words;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.btnBack.setPadding(
                binding.btnBack.getPaddingLeft(),
                binding.btnBack.getPaddingTop() + top,
                binding.btnBack.getPaddingRight(),
                binding.btnBack.getPaddingBottom()
            );
            binding.scrollView.setPadding(
                binding.scrollView.getPaddingLeft(),
                binding.scrollView.getPaddingTop(),
                binding.scrollView.getPaddingRight(),
                bottom
            );
            return insets;
        });

        String subcategoryId = getIntent().getStringExtra(EXTRA_SUBCATEGORY_ID);
        String iconUrl = getIntent().getStringExtra(EXTRA_ICON_URL);
        currentIndex = getIntent().getIntExtra(EXTRA_WORD_INDEX, 0);

        if (subcategoryId == null) {
            finish();
            return;
        }

        binding.btnBack.setOnClickListener(v -> finish());

        if (iconUrl != null && !iconUrl.isEmpty()) {
            Coil.imageLoader(this).enqueue(
                new ImageRequest.Builder(this).data(iconUrl).crossfade(true).target(binding.imgHero).build());
        }

        WordDetailViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(WordDetailViewModel.class);
        viewModel.getWords().observe(this, wordList -> {
            if (wordList == null || wordList.isEmpty()) return;
            words = wordList;
            showWord(currentIndex);
        });
        viewModel.load(Long.parseLong(subcategoryId));
    }

    private void showWord(int index) {
        if (words == null || index >= words.size()) return;
        Word word = words.get(index);

        binding.txtKanji.setText(word.kanji);
        binding.txtReading.setText(word.reading);
        binding.txtRomaji.setText(word.romaji);
        binding.txtEn.setText(word.en);

        if (word.isLocked) {
            binding.cardExample.setVisibility(View.GONE);
            binding.cardLocked.setVisibility(View.VISIBLE);
            binding.txtLockCost.setText("Unlock for " + word.coinPrice + " coins");
            binding.btnUnlockWord.setOnClickListener(v -> fetchCoinsAndUnlock(word));
        } else {
            binding.cardExample.setVisibility(View.VISIBLE);
            binding.cardLocked.setVisibility(View.GONE);
            binding.txtExampleJp.setText(word.example_jp);
            binding.txtExampleRomaji.setText(word.example_romaji);
            binding.txtExampleEn.setText(word.example_en);
        }

        binding.chipType.setText(word.type.isEmpty() ? "—" : word.type);
        binding.chipJlpt.setText(word.jlpt.isEmpty() ? "JLPT" : word.jlpt);
        binding.chipXp.setText("+" + word.xp + " XP");

        boolean isLast = index >= words.size() - 1;
        binding.btnNextFinish.setText(isLast ? "Finish" : "Next");
        binding.btnNextFinish.setOnClickListener(v -> {
            if (!isLast) {
                currentIndex++;
                showWord(currentIndex);
            } else {
                openScore();
            }
        });
    }

    private void openScore() {
        NavHostFragment nhf = NavHostFragment.create(R.navigation.nav_subcategory);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.navHostContainer, nhf)
            .commitNow();
        binding.navHostContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (binding.navHostContainer.getVisibility() == View.VISIBLE) {
            NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostContainer);
            if (nhf != null && nhf.getNavController().popBackStack()) {
                return;
            }
            if (nhf != null) {
                getSupportFragmentManager().beginTransaction().remove(nhf).commitNow();
            }
            binding.navHostContainer.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void fetchCoinsAndUnlock(Word word) {
        ServiceLocator.api.coinsBalance().enqueue(new Callback<CoinsBalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoinsBalanceResponse> call, @NonNull Response<CoinsBalanceResponse> response) {
                if (isFinishing()) return;
                int coins = response.isSuccessful() && response.body() != null ? response.body().coins : 0;
                showUnlockSheet(word, coins);
            }

            @Override
            public void onFailure(@NonNull Call<CoinsBalanceResponse> call, @NonNull Throwable t) {
                if (isFinishing()) return;
                showUnlockSheet(word, 0);
            }
        });
    }

    private void showUnlockSheet(Word word, int userCoins) {
        UnlockBottomSheet sheet = UnlockBottomSheet.create(
            UnlockBottomSheet.Type.WORD,
            word.id,
            word.kanji + " · " + word.en,
            word.coinPrice,
            userCoins
        );
        sheet.setOnUnlockListener(() -> ServiceLocator.categoryRepository.refresh());
        sheet.show(getSupportFragmentManager(), "unlock");
    }
}
