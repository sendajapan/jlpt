package com.scholarlyapps.pathlingo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.models.Subcategory;
import java.util.List;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder> {
    private Context context;
    private List<Subcategory> subcategories;
    private String categoryId;
    private OnSubcategoryClickListener listener;

    public interface OnSubcategoryClickListener {
        void onSubcategoryClick(String categoryId, Subcategory subcategory);
    }

    public SubcategoryAdapter(Context context, List<Subcategory> subcategories, String categoryId) {
        this.context = context;
        this.subcategories = subcategories;
        this.categoryId = categoryId;
    }

    public void setOnSubcategoryClickListener(OnSubcategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubcategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_subcategory, parent, false);
        return new SubcategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryViewHolder holder, int position) {
        holder.bind(subcategories.get(position));
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public class SubcategoryViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView jpView;
        private TextView enView;
        private ProgressBar progressBar;
        private TextView masteryView;
        private TextView lockView;

        public SubcategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            jpView = itemView.findViewById(R.id.jp_text);
            enView = itemView.findViewById(R.id.en_text);
            progressBar = itemView.findViewById(R.id.progress_bar);
            masteryView = itemView.findViewById(R.id.mastery_text);
            lockView = itemView.findViewById(R.id.lock_text);
        }

        public void bind(Subcategory subcat) {
            jpView.setText(subcat.jp);
            enView.setText(subcat.en);
            progressBar.setProgress((int) ((subcat.mastered * 100) / (float) subcat.total));
            masteryView.setText(subcat.mastered + "/" + subcat.total);
            lockView.setVisibility(subcat.locked ? View.VISIBLE : View.GONE);

            int bgRes = getBgResource(subcat.bg);
            cardView.setCardBackgroundColor(context.getColor(bgRes));

            cardView.setOnClickListener(v -> {
                if (!subcat.locked && !subcat.words.isEmpty() && listener != null) {
                    listener.onSubcategoryClick(categoryId, subcat);
                }
            });
        }

        private int getBgResource(String bg) {
            switch (bg) {
                case "sage": return R.color.sage;
                case "butter": return R.color.butter;
                case "lav": return R.color.lav;
                case "sky": return R.color.sky;
                case "rose": return R.color.rose;
                case "terra": return R.color.terra;
                default: return R.color.sage;
            }
        }
    }
}
