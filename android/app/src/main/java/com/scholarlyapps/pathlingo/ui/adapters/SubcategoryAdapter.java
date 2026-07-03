package com.scholarlyapps.pathlingo.ui.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ItemSubcategoryCardBinding;
import com.scholarlyapps.pathlingo.models.Subcategory;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

    public interface OnSubcategoryClick {
        void onClick(Subcategory subcategory);
        void onUnlockClick(Subcategory subcategory);
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
            binding.imgLock.setMinFrame(33);
            binding.imgLock.setMaxFrame(59);
            binding.progressBar.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        void bind(Subcategory sub, OnSubcategoryClick listener) {
            binding.txtEn.setText(sub.en);
            binding.txtJp.setText(sub.jp);
            binding.txtRomaji.setText(sub.romaji);

            binding.imgLock.setVisibility(sub.isLocked ? View.VISIBLE : View.GONE);

            int accentColor = accentColor(sub.bg, itemView.getContext());

            binding.progressBar.cancelAnimation();
            binding.progressBar.setMinProgress(0f);
            binding.progressBar.setMaxProgress(0.75f);
            binding.progressBar.playAnimation();
            binding.txtCount.setText(sub.mastered + "/" + sub.total);

            if (sub.img != null && !sub.img.isEmpty()) {
                Coil.imageLoader(binding.imgBg.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgBg.getContext())
                                .data(sub.img)
                                .crossfade(true)
                                .target(binding.imgBg)
                                .build()
                );
            } else {
                binding.imgBg.setImageDrawable(new ColorDrawable(accentColor));
            }

            if (sub.iconUrl != null && !sub.iconUrl.isEmpty()) {
                Coil.imageLoader(binding.imgIcon.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgIcon.getContext())
                                .data(sub.iconUrl)
                                .crossfade(true)
                                .target(binding.imgIcon)
                                .build()
                );
            }

            if (sub.isLocked) {
                itemView.setOnClickListener(v -> listener.onUnlockClick(sub));
            } else {
                itemView.setOnClickListener(v -> listener.onClick(sub));
            }
            itemView.setAlpha(1f);
        }

        private int accentColor(String bg, Context context) {
            switch (bg) {
                case "butter": return ContextCompat.getColor(context, R.color.butter);
                case "lav":    return ContextCompat.getColor(context, R.color.lav);
                case "sky":    return ContextCompat.getColor(context, R.color.sky);
                case "rose":   return ContextCompat.getColor(context, R.color.rose);
                case "terra":  return ContextCompat.getColor(context, R.color.terra);
                default:       return ContextCompat.getColor(context, R.color.sage_deep);
            }
        }
    }
}
