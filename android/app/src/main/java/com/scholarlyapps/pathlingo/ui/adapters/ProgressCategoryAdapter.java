package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.databinding.ItemCategoryProgressBinding;
import com.scholarlyapps.pathlingo.models.Category;

import java.util.List;

public class ProgressCategoryAdapter extends RecyclerView.Adapter<ProgressCategoryAdapter.ViewHolder> {

    private List<Category> categories;

    public ProgressCategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public void setData(List<Category> data) {
        this.categories = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryProgressBinding binding = ItemCategoryProgressBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemCategoryProgressBinding binding;

        ViewHolder(ItemCategoryProgressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category cat) {
            binding.txtCategoryName.setText(cat.en);
            binding.txtPercent.setText(cat.progress + "%");
            binding.progressBar.setProgress(cat.progress);
            binding.txtWordCount.setText(cat.count + " words");
        }
    }
}
