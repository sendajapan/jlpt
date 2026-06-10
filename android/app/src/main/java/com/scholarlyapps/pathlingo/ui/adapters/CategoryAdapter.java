package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.databinding.ItemCategoryCardBinding;
import com.scholarlyapps.pathlingo.databinding.ItemCategoryViewAllBinding;
import com.scholarlyapps.pathlingo.models.Category;

import java.util.ArrayList;
import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CATEGORY = 0;
    private static final int TYPE_VIEW_ALL = 1;
    private static final int MAX_CATEGORIES = 5;

    public interface OnCategoryClick {
        void onClick(Category category);
        void onUnlockClick(Category category);
        void onViewAllClick();
    }

    private List<Category> categories;
    private final boolean showAll;
    private final OnCategoryClick listener;

    public CategoryAdapter(List<Category> categories, OnCategoryClick listener) {
        this(categories, false, listener);
    }

    public CategoryAdapter(List<Category> categories, boolean showAll, OnCategoryClick listener) {
        this.categories = categories;
        this.showAll = showAll;
        this.listener = listener;
    }

    public void setData(List<Category> data) {
        this.categories = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (showAll || position < Math.min(categories.size(), MAX_CATEGORIES)) return TYPE_CATEGORY;
        return TYPE_VIEW_ALL;
    }

    @Override
    public int getItemCount() {
        if (showAll) return categories.size();
        return Math.min(categories.size(), MAX_CATEGORIES) + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_VIEW_ALL) {
            return new ViewAllHolder(ItemCategoryViewAllBinding.inflate(inflater, parent, false));
        }
        return new CategoryHolder(ItemCategoryCardBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryHolder) {
            ((CategoryHolder) holder).bind(categories.get(position), listener);
        } else if (holder instanceof ViewAllHolder) {
            ((ViewAllHolder) holder).bind(listener);
        }
    }

    static class CategoryHolder extends RecyclerView.ViewHolder {

        final ItemCategoryCardBinding binding;

        CategoryHolder(ItemCategoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category cat, OnCategoryClick listener) {
            binding.txtJp.setText(cat.jp);
            binding.txtEn.setText(cat.en);

            if (cat.bg != null && !cat.bg.isEmpty()) {
                Coil.imageLoader(binding.imgCategory.getContext()).enqueue(
                    new ImageRequest.Builder(binding.imgCategory.getContext())
                        .data(cat.bg)
                        .crossfade(true)
                        .target(binding.imgCategory)
                        .build()
                );
            }
            if (cat.iconUrl != null && !cat.iconUrl.isEmpty()) {
                Coil.imageLoader(binding.imgIcon.getContext()).enqueue(
                    new ImageRequest.Builder(binding.imgIcon.getContext())
                        .data(cat.iconUrl)
                        .crossfade(true)
                        .target(binding.imgIcon)
                        .build()
                );
            }

            if (cat.isLocked) {
                binding.cardRoot.setOnClickListener(v -> listener.onUnlockClick(cat));
            } else {
                binding.cardRoot.setOnClickListener(v -> listener.onClick(cat));
            }
        }
    }

    static class ViewAllHolder extends RecyclerView.ViewHolder {

        ViewAllHolder(ItemCategoryViewAllBinding binding) {
            super(binding.getRoot());
        }

        void bind(OnCategoryClick listener) {
            itemView.setOnClickListener(v -> listener.onViewAllClick());
        }
    }
}
