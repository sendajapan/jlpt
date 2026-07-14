package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.data.remote.dto.KanjiStrokeGroupDto;
import com.scholarlyapps.pathlingo.databinding.ItemKanjiStrokeCardBinding;

import java.util.ArrayList;
import java.util.List;

public class KanjiStrokeAdapter extends RecyclerView.Adapter<KanjiStrokeAdapter.ViewHolder> {

    public interface OnStrokeClick {
        void onClick(KanjiStrokeGroupDto group);
    }

    private final List<KanjiStrokeGroupDto> groups = new ArrayList<>();
    private final OnStrokeClick listener;

    public KanjiStrokeAdapter(OnStrokeClick listener) {
        this.listener = listener;
    }

    public void submit(List<KanjiStrokeGroupDto> data) {
        groups.clear();
        groups.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKanjiStrokeCardBinding binding = ItemKanjiStrokeCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(groups.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemKanjiStrokeCardBinding binding;

        ViewHolder(ItemKanjiStrokeCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(KanjiStrokeGroupDto group, OnStrokeClick listener) {
            binding.txtStrokes.setText(String.valueOf(group.strokes));
            binding.txtCount.setText(group.count + (group.count == 1 ? " Kanji" : " Kanjis"));
            itemView.setOnClickListener(v -> listener.onClick(group));
        }
    }
}
