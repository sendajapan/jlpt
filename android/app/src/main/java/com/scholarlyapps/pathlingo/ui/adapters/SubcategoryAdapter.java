package com.scholarlyapps.pathlingo.ui.adapters;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ItemSubcategoryCardBinding;
import com.scholarlyapps.pathlingo.models.Subcategory;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

    public interface OnSubcategoryClick {
        void onClick(Subcategory subcategory);
    }

    private final List<Subcategory> subcategories;
    private final OnSubcategoryClick listener;

    public SubcategoryAdapter(List<Subcategory> subcategories, OnSubcategoryClick listener) {
        this.subcategories = subcategories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubcategoryCardBinding binding = ItemSubcategoryCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(subcategories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemSubcategoryCardBinding binding;

        ViewHolder(ItemSubcategoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Subcategory sub, OnSubcategoryClick listener) {
            binding.txtJp.setText(sub.jp);
            binding.txtEn.setText(sub.en);
            binding.txtLock.setVisibility(sub.locked ? View.VISIBLE : View.GONE);

            int accentColor = accentColor(sub.bg, binding.accentBar);
            GradientDrawable barBg = new GradientDrawable();
            barBg.setShape(GradientDrawable.RECTANGLE);
            barBg.setCornerRadius(8f);
            barBg.setColor(accentColor);
            binding.accentBar.setBackground(barBg);
            binding.progressBar.setProgressTintList(ColorStateList.valueOf(accentColor));

            int progress = sub.total > 0 ? (sub.mastered * 100 / sub.total) : 0;
            binding.progressBar.setProgress(progress);
            binding.txtWordProgress.setText(sub.mastered + " / " + sub.total + " words");

            if (!sub.iconUrl.isEmpty()) {
                binding.imgIcon.setVisibility(View.VISIBLE);
                Coil.imageLoader(binding.imgIcon.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgIcon.getContext())
                                .data(sub.iconUrl)
                                .target(binding.imgIcon)
                                .build()
                );
            } else {
                binding.imgIcon.setVisibility(View.GONE);
            }

            boolean enabled = !sub.locked && !sub.words.isEmpty();
            itemView.setAlpha(enabled ? 1f : 0.5f);
            itemView.setOnClickListener(enabled ? v -> listener.onClick(sub) : null);
        }

        private int accentColor(String bg, View view) {
            switch (bg) {
                case "butter": return ContextCompat.getColor(view.getContext(), R.color.butter);
                case "lav": return ContextCompat.getColor(view.getContext(), R.color.lav);
                case "sky": return ContextCompat.getColor(view.getContext(), R.color.sky);
                case "rose": return ContextCompat.getColor(view.getContext(), R.color.rose);
                case "terra": return ContextCompat.getColor(view.getContext(), R.color.terra);
                default: return ContextCompat.getColor(view.getContext(), R.color.sage_deep);
            }
        }
    }
}
