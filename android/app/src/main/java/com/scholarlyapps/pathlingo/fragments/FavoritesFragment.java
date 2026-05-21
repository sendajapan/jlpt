package com.scholarlyapps.pathlingo.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.models.Word;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Word> favorites = new ArrayList<>();
        for (Category cat : DataManager.getInstance().getCategories()) {
            for (Subcategory sub : cat.subcategories) {
                for (Word word : sub.words) {
                    if (word.favorite) favorites.add(word);
                }
            }
        }

        RecyclerView list = view.findViewById(R.id.favorites_list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        list.setAdapter(new WordAdapter(favorites));
    }

    private static class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
        private final List<Word> words;

        WordAdapter(List<Word> words) { this.words = words; }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
            return new WordViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder h, int pos) {
            Word w = words.get(pos);
            h.kanji.setText(w.kanji);
            h.reading.setText(w.reading + " · " + w.romaji);
            h.english.setText(w.en);
        }

        @Override
        public int getItemCount() { return words.size(); }

        static class WordViewHolder extends RecyclerView.ViewHolder {
            TextView kanji, reading, english;
            WordViewHolder(@NonNull View v) {
                super(v);
                kanji   = v.findViewById(R.id.kanji);
                reading = v.findViewById(R.id.reading);
                english = v.findViewById(R.id.english);
            }
        }
    }
}
