package com.scholarlyapps.pathlingo.ui.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.databinding.ItemKanjiLevelCardBinding;

import java.util.List;

public class KanjiLevelAdapter extends RecyclerView.Adapter<KanjiLevelAdapter.ViewHolder> {

    public interface OnLevelClick {
        void onClick(String level);
    }

    public static class Level {
        public final String name;
        public final String subtitle;

        @ColorRes
        public final int accentColor;

        @ColorRes
        public final int backgroundColor;

        public Level(String name, String subtitle, @ColorRes int accentColor, @ColorRes int backgroundColor) {
            this.name = name;
            this.subtitle = subtitle;
            this.accentColor = accentColor;
            this.backgroundColor = backgroundColor;
        }
    }

    private final List<Level> levels;
    private final OnLevelClick listener;

    public KanjiLevelAdapter(List<Level> levels, OnLevelClick listener) {
        this.levels = levels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKanjiLevelCardBinding binding = ItemKanjiLevelCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(levels.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemKanjiLevelCardBinding binding;

        ViewHolder(ItemKanjiLevelCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Level level, OnLevelClick listener) {
            int accent = ContextCompat.getColor(itemView.getContext(), level.accentColor);
            int background = ContextCompat.getColor(itemView.getContext(), level.backgroundColor);
            binding.txtLevel.setText(level.name);
            binding.txtLevel.setBackgroundTintList(ColorStateList.valueOf(accent));
            binding.txtSubtitle.setText(level.subtitle);
            binding.imgChevron.setImageTintList(ColorStateList.valueOf(accent));
            binding.cardRoot.setCardBackgroundColor(background);
            binding.cardRoot.setStrokeColor(accent);
            itemView.setOnClickListener(v -> listener.onClick(level.name));
        }
    }
}
