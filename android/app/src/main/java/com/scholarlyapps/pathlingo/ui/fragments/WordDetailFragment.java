package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.databinding.FragmentWordDetailBinding;
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

public class WordDetailFragment extends Fragment {

    private FragmentWordDetailBinding binding;
    private List<Word> words;
    private int currentIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWordDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String subcategoryIdStr = args != null ? args.getString("subcategoryId") : null;
        String iconUrl = args != null ? args.getString("iconUrl", "") : "";
        currentIndex = args != null ? args.getInt("wordIndex", 0) : 0;

        if (subcategoryIdStr == null) {
            Navigation.findNavController(view).popBackStack();
            return;
        }

        WordDetailViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(WordDetailViewModel.class);

        binding.btnBack.setOnClickListener(v -> {
            if (!Navigation.findNavController(v).popBackStack()) {
                requireActivity().onBackPressed();
            }
        });

        if (!iconUrl.isEmpty()) {
            Coil.imageLoader(requireContext()).enqueue(
                new ImageRequest.Builder(requireContext()).data(iconUrl).target(binding.imgHero).build());
        }

        viewModel.getWords().observe(getViewLifecycleOwner(), wordList -> {
            if (wordList == null || wordList.isEmpty()) return;
            words = wordList;
            showWord(currentIndex);
        });

        viewModel.load(Long.parseLong(subcategoryIdStr));
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
                Navigation.findNavController(v).navigate(R.id.action_word_to_score);
            }
        });
    }

    private void fetchCoinsAndUnlock(Word word) {
        ServiceLocator.api.coinsBalance().enqueue(new Callback<CoinsBalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoinsBalanceResponse> call, @NonNull Response<CoinsBalanceResponse> response) {
                if (!isAdded()) return;
                int coins = response.isSuccessful() && response.body() != null ? response.body().coins : 0;
                showUnlockSheet(word, coins);
            }

            @Override
            public void onFailure(@NonNull Call<CoinsBalanceResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
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
        sheet.show(getChildFragmentManager(), "unlock");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
