package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.databinding.ItemCategoryCardBinding;
import com.scholarlyapps.pathlingo.models.Category;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClick {
        void onClick(Category category);
    }

    private final List<Category> categories;
    private final OnCategoryClick listener;

    public CategoryAdapter(List<Category> categories, OnCategoryClick listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryCardBinding binding = ItemCategoryCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemCategoryCardBinding binding;

        ViewHolder(ItemCategoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category cat, OnCategoryClick listener) {
            binding.txtJp.setText(cat.jp);
            binding.txtCh.setText(cat.ch);
            binding.txtEn.setText(cat.en);
            binding.txtWordCount.setText(cat.count + " words");
            binding.txtLock.setVisibility(cat.locked ? View.VISIBLE : View.GONE);

            if (!cat.bg.isEmpty()) {
                binding.imgBackground.setVisibility(View.VISIBLE);
                Coil.imageLoader(binding.imgBackground.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgBackground.getContext())
                                .data(cat.bg)
                                .target(binding.imgBackground)
                                .build()
                );
            } else {
                binding.imgBackground.setVisibility(View.GONE);
            }

            if (!cat.iconUrl.isEmpty()) {
                binding.imgIcon.setVisibility(View.VISIBLE);
                Coil.imageLoader(binding.imgIcon.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgIcon.getContext())
                                .data(cat.iconUrl)
                                .target(binding.imgIcon)
                                .build()
                );
            } else {
                binding.imgIcon.setVisibility(View.GONE);
            }

            binding.cardRoot.setOnClickListener(v -> listener.onClick(cat));
        }
    }
}
