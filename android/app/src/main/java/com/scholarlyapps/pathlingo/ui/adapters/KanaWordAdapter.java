package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.data.remote.dto.KanaWordDto;
import com.scholarlyapps.pathlingo.databinding.ItemKanaWordBinding;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class KanaWordAdapter extends RecyclerView.Adapter<KanaWordAdapter.ViewHolder> {

    private final List<KanaWordDto> words;

    public KanaWordAdapter(List<KanaWordDto> words) {
        this.words = words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKanaWordBinding binding = ItemKanaWordBinding.inflate(
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

        final ItemKanaWordBinding binding;

        ViewHolder(ItemKanaWordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(KanaWordDto word) {
            binding.txtEn.setText(word.wordEn);
            binding.txtJp.setText(word.wordJp);
            binding.txtRomaji.setText(word.wordRomaji);

            if (word.imageThumbnailUrl != null && !word.imageThumbnailUrl.isEmpty()) {
                Coil.imageLoader(binding.imgWord.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgWord.getContext())
                                .data(word.imageThumbnailUrl)
                                .crossfade(true)
                                .target(binding.imgWord)
                                .build()
                );
            }
        }
    }
}
