package com.scholarlyapps.pathlingo.ui.adapters;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ItemWordListCardBinding;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    public interface OnWordClick {
        void onClick(Word word);
    }

    private final List<Word> words;
    private final OnWordClick listener;

    public WordListAdapter(List<Word> words, OnWordClick listener) {
        this.words = words;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWordListCardBinding binding = ItemWordListCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(words.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemWordListCardBinding binding;

        ViewHolder(ItemWordListCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Word word, OnWordClick listener) {
            binding.txtEn.setText(word.en);
            binding.txtJp.setText(word.kanji);
            binding.txtRomaji.setText(word.romaji);

            if (word.bgUrl != null && !word.bgUrl.isEmpty()) {
                Coil.imageLoader(binding.imgBg.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgBg.getContext())
                                .data(word.bgUrl)
                                .crossfade(true)
                                .target(binding.imgBg)
                                .build()
                );
            } else {
                binding.imgBg.setImageDrawable(new ColorDrawable(
                        ContextCompat.getColor(binding.imgBg.getContext(), R.color.color_theme_extra_light)));
            }

            if (word.img != null && !word.img.isEmpty()) {
                Coil.imageLoader(binding.imgIcon.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgIcon.getContext())
                                .data(word.img)
                                .crossfade(true)
                                .target(binding.imgIcon)
                                .build()
                );
            }

            itemView.setOnClickListener(v -> listener.onClick(word));
        }
    }
}
