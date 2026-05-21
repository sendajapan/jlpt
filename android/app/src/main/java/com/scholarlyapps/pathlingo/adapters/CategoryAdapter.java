package com.scholarlyapps.pathlingo.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import coil.Coil;
import coil.request.ImageRequest;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.models.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private final List<Category> categories;
    private OnCategoryClickListener listener;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final View bgView;
        private final ImageView categoryImage;
        private final TextView tvChar;
        private final TextView jpName;
        private final TextView enName;
        private final TextView lockIcon;
        private final ProgressBar progressBar;
        private final TextView wordCount;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView      = itemView.findViewById(R.id.card_view);
            bgView        = itemView.findViewById(R.id.bg_view);
            categoryImage = itemView.findViewById(R.id.category_image);
            tvChar        = itemView.findViewById(R.id.tv_char);
            jpName        = itemView.findViewById(R.id.jp_name);
            enName        = itemView.findViewById(R.id.en_name);
            lockIcon      = itemView.findViewById(R.id.lock_icon);
            progressBar   = itemView.findViewById(R.id.progress_bar);
            wordCount     = itemView.findViewById(R.id.word_count);
        }

        void bind(Category category, OnCategoryClickListener listener) {
            Context ctx = itemView.getContext();

            // Gradient background on dedicated bg_view
            int bgRes = ctx.getResources().getIdentifier(
                    "bg_cat_" + category.id + "_rounded", "drawable", ctx.getPackageName());
            if (bgRes != 0 && bgView != null) {
                cardView.setCardBackgroundColor(Color.TRANSPARENT);
                bgView.setBackgroundResource(bgRes);
            }

            // Ink color for text
            int inkRes = ctx.getResources().getIdentifier(
                    "cat_" + category.id + "_ink", "color", ctx.getPackageName());
            int inkColor = inkRes != 0
                    ? ContextCompat.getColor(ctx, inkRes)
                    : ContextCompat.getColor(ctx, R.color.ink);

            if (categoryImage != null && category.img != null && !category.img.isEmpty()) {
                if (category.img.startsWith("http")) {
                    ImageRequest request = new ImageRequest.Builder(ctx)
                            .data(category.img)
                            .target(categoryImage)
                            .crossfade(true)
                            .build();
                    Coil.imageLoader(ctx).enqueue(request);
                } else {
                    int imgRes = ctx.getResources().getIdentifier(
                            category.img, "drawable", ctx.getPackageName());
                    if (imgRes != 0) categoryImage.setImageResource(imgRes);
                }
            }

            // Kanji char watermark
            if (tvChar != null) tvChar.setText(category.ch);

            // Names
            if (jpName != null) { jpName.setText(category.jp); jpName.setTextColor(inkColor); }
            if (enName != null) { enName.setText(category.en); enName.setTextColor(inkColor); }

            // Lock
            if (lockIcon != null) lockIcon.setVisibility(category.locked ? View.VISIBLE : View.GONE);

            // Progress
            if (progressBar != null) {
                progressBar.setProgress(category.progress);
                progressBar.setProgressTintList(ColorStateList.valueOf(inkColor));
            }
            if (wordCount != null) {
                wordCount.setText(category.count + " words");
                wordCount.setTextColor(inkColor);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onCategoryClick(category);
            });
        }
    }
}
