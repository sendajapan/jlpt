package com.scholarlyapps.pathlingo.ui.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.User;
import java.util.List;

public class ProgressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = DataManager.getInstance().getUser();
        if (user != null) {
            ((TextView) view.findViewById(R.id.tv_level)).setText("Level " + user.level);
            ((TextView) view.findViewById(R.id.tv_xp)).setText(user.xp + " / " + user.maxXp + " XP");
            ((TextView) view.findViewById(R.id.tv_streak)).setText("🔥 " + user.streak);
            int xpPercent = (int) ((user.xp / (float) user.maxXp) * 100);
            ((ProgressBar) view.findViewById(R.id.xp_bar)).setProgress(xpPercent);
        }

        List<Category> categories = DataManager.getInstance().getCategories();
        RecyclerView list = view.findViewById(R.id.progress_list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        list.setAdapter(new CategoryProgressAdapter(categories));
    }

    private static class CategoryProgressAdapter
            extends RecyclerView.Adapter<CategoryProgressAdapter.ViewHolder> {

        private final List<Category> categories;

        CategoryProgressAdapter(List<Category> categories) { this.categories = categories; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_progress_category, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
            Category cat = categories.get(pos);
            h.name.setText(cat.en);
            h.percent.setText(cat.progress + "%");
            h.progressBar.setProgress(cat.progress);
            h.wordCount.setText(cat.count + " words");
        }

        @Override
        public int getItemCount() { return categories.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, percent, wordCount;
            ProgressBar progressBar;
            ViewHolder(@NonNull View v) {
                super(v);
                name        = v.findViewById(R.id.category_name);
                percent     = v.findViewById(R.id.progress_percent);
                progressBar = v.findViewById(R.id.progress_bar);
                wordCount   = v.findViewById(R.id.word_count);
            }
        }
    }
}
