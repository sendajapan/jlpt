package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.dto.KanaDto;
import com.scholarlyapps.pathlingo.databinding.ItemKanaCellBinding;

import java.util.List;

import java.util.Locale;

public class KanaGridAdapter extends RecyclerView.Adapter<KanaGridAdapter.ViewHolder> {

    public interface OnKanaClick {
        void onClick(KanaDto kana);
    }

    private final List<KanaDto> cells;
    private final OnKanaClick listener;

    public KanaGridAdapter(List<KanaDto> cells, OnKanaClick listener) {
        this.cells = cells;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKanaCellBinding binding = ItemKanaCellBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cells.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return cells.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemKanaCellBinding binding;

        ViewHolder(ItemKanaCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(KanaDto kana, OnKanaClick listener) {
            if (kana == null) {
                binding.cellContainer.setVisibility(View.INVISIBLE);
                binding.imgLearned.setVisibility(View.GONE);
                itemView.setOnClickListener(null);
                itemView.setClickable(false);
                return;
            }

            binding.cellContainer.setVisibility(View.VISIBLE);
            binding.txtKana.setText(kana.kana);
            binding.txtRomaji.setText(kana.romaji != null ? kana.romaji.toLowerCase(Locale.ROOT).trim() : "");
            binding.cellContainer.setBackgroundResource(kana.isLearned
                    ? R.drawable.bg_kana_cell_learned
                    : R.drawable.bg_kana_cell);
            binding.imgLearned.setVisibility(kana.isLearned ? View.VISIBLE : View.GONE);
            itemView.setOnClickListener(v -> listener.onClick(kana));
        }
    }
}
