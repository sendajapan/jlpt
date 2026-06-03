package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.databinding.ItemWordCardBinding;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    private final List<Word> words;

    public WordAdapter(List<Word> words) {
        this.words = words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWordCardBinding binding = ItemWordCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemWordCardBinding binding;

        ViewHolder(ItemWordCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Word word) {
            binding.txtKanji.setText(word.kanji);
            binding.txtReadingRomaji.setText(word.reading + " · " + word.romaji);
            binding.txtEn.setText(word.en);
        }
    }
}
