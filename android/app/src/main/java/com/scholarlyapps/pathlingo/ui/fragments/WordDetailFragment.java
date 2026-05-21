package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.Word;

public class WordDetailFragment extends Fragment {

    private String categoryId;
    private String subcategoryId;
    private int wordIndex;
    private Word currentWord;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryId = requireArguments().getString("categoryId");
        subcategoryId = requireArguments().getString("subcategoryId");
        wordIndex = requireArguments().getInt("wordIndex", 0);

        DataManager dm = DataManager.getInstance();
        Category category = dm.getCategoryById(categoryId);
        currentWord = category.subcategories.stream()
            .filter(s -> s.id.equals(subcategoryId))
            .findFirst()
            .orElseThrow()
            .words.get(wordIndex);

        displayWord(view, currentWord);

        Button btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_wordDetail_to_score);
        });

        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void displayWord(View view, Word word) {
        ((TextView) view.findViewById(R.id.kanji)).setText(word.kanji);
        ((TextView) view.findViewById(R.id.reading)).setText(word.reading);
        ((TextView) view.findViewById(R.id.romaji)).setText(word.romaji);
        ((TextView) view.findViewById(R.id.english)).setText(word.en);
        ((TextView) view.findViewById(R.id.example_jp)).setText(word.example_jp);
        ((TextView) view.findViewById(R.id.example_romaji)).setText(word.example_romaji);
        ((TextView) view.findViewById(R.id.example_en)).setText(word.example_en);
    }
}
