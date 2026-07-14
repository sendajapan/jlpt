package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.dto.KanjiDto;
import com.scholarlyapps.pathlingo.databinding.ItemKanjiCellBinding;
import com.scholarlyapps.pathlingo.databinding.ItemLoadingFooterBinding;

import java.util.ArrayList;
import java.util.List;

public class KanjiGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnKanjiClick {
        void onClick(KanjiDto kanji);
    }

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private final List<KanjiDto> items = new ArrayList<>();
    private final OnKanjiClick listener;
    private boolean showFooter = false;

    public KanjiGridAdapter(OnKanjiClick listener) {
        this.listener = listener;
    }

    public void submit(List<KanjiDto> kanjis) {
        items.clear();
        items.addAll(kanjis);
        notifyDataSetChanged();
    }

    public void setShowFooter(boolean show) {
        if (showFooter == show) return;
        showFooter = show;
        notifyDataSetChanged();
    }

    public boolean isFooter(int position) {
        return showFooter && position == items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isFooter(position) ? TYPE_FOOTER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(ItemLoadingFooterBinding.inflate(inflater, parent, false));
        }
        return new ItemViewHolder(ItemKanjiCellBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bind(items.get(position), listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + (showFooter ? 1 : 0);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        final ItemKanjiCellBinding binding;

        ItemViewHolder(ItemKanjiCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(KanjiDto kanji, OnKanjiClick listener) {
            binding.txtKanji.setText(kanji.kanji);
            binding.txtMeaning.setText(meaning(kanji));
            binding.cellContainer.setBackgroundResource(kanji.isLearned
                    ? R.drawable.bg_kana_cell_learned
                    : R.drawable.bg_kana_cell);
            binding.imgLearned.setVisibility(kanji.isLearned ? View.VISIBLE : View.GONE);
            itemView.setOnClickListener(v -> listener.onClick(kanji));
        }

        private String meaning(KanjiDto kanji) {
            if (kanji.translate != null && !kanji.translate.isEmpty()) return kanji.translate;
            if (kanji.meanings != null && !kanji.meanings.isEmpty()) return kanji.meanings.split(",")[0].trim();
            return "";
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {

        FooterViewHolder(ItemLoadingFooterBinding binding) {
            super(binding.getRoot());
        }
    }
}
