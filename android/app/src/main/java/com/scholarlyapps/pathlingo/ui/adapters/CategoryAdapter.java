package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ItemCategoryViewAllBinding;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.ui.utils.ShimmerImage;

import java.util.List;

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
        int layoutRes = R.layout.item_category_card;
        return new CategoryHolder(inflater.inflate(layoutRes, parent, false));
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

        final ImageView imgCategory;
        final ImageView imgIcon;
        final ShimmerFrameLayout bgShimmer;
        final ShimmerFrameLayout iconShimmer;
        final TextView txtJp;
        final TextView txtEn;
        final View cardRoot;

        CategoryHolder(View view) {
            super(view);
            imgCategory = view.findViewById(R.id.imgCategory);
            imgIcon = view.findViewById(R.id.imgIcon);
            bgShimmer = view.findViewById(R.id.bgShimmer);
            iconShimmer = view.findViewById(R.id.iconShimmer);
            txtJp = view.findViewById(R.id.txtJp);
            txtEn = view.findViewById(R.id.txtEn);
            cardRoot = view.findViewById(R.id.cardRoot);
        }

        void bind(Category cat, OnCategoryClick listener) {
            txtJp.setText(cat.jp);
            txtEn.setText(cat.en);

            ShimmerImage.load(bgShimmer, imgCategory, cat.bg);
            ShimmerImage.load(iconShimmer, imgIcon, cat.iconUrl);

            if (cat.isLocked) {
                cardRoot.setOnClickListener(v -> listener.onUnlockClick(cat));
            } else {
                cardRoot.setOnClickListener(v -> listener.onClick(cat));
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
