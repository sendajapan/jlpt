package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.databinding.FragmentWordDetailBinding;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

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
        String categoryId = args != null ? args.getString("categoryId") : null;
        String subcategoryId = args != null ? args.getString("subcategoryId") : null;
        currentIndex = args != null ? args.getInt("wordIndex", 0) : 0;

        Category category = DataManager.getInstance().getCategoryById(categoryId);
        Subcategory subcat = category != null
                ? category.subcategories.stream().filter(s -> s.id.equals(subcategoryId)).findFirst().orElse(null)
                : null;

        if (subcat == null) {
            Navigation.findNavController(view).popBackStack();
            return;
        }

        words = subcat.words;

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        if (!subcat.iconUrl.isEmpty()) {
            Coil.imageLoader(requireContext()).enqueue(
                    new ImageRequest.Builder(requireContext()).data(subcat.iconUrl).target(binding.imgHero).build());
        }

        showWord(currentIndex);
    }

    private void showWord(int index) {
        Word word = words.get(index);

        binding.txtKanji.setText(word.kanji);
        binding.txtReading.setText(word.reading);
        binding.txtRomaji.setText(word.romaji);
        binding.txtEn.setText(word.en);
        binding.txtExampleJp.setText(word.example_jp);
        binding.txtExampleRomaji.setText(word.example_romaji);
        binding.txtExampleEn.setText(word.example_en);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
