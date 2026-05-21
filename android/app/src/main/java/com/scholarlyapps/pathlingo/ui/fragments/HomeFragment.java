package com.scholarlyapps.pathlingo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.activities.CategoryListActivity;
import com.scholarlyapps.pathlingo.activities.SubcategoryActivity;
import com.scholarlyapps.pathlingo.adapters.CategoryAdapter;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataManager dm = DataManager.getInstance();
        User user = dm.getUser();

        TextView tvDay = view.findViewById(R.id.tv_day);
        tvDay.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(new Date()));

        TextView tvName = view.findViewById(R.id.tv_user_name);
        if (user != null && user.name != null) {
            tvName.setText(user.name);
        }

        RecyclerView categoryGrid = view.findViewById(R.id.category_grid);
        categoryGrid.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        CategoryAdapter adapter = new CategoryAdapter(dm.getCategories());
        adapter.setOnCategoryClickListener(category -> {
            Intent intent = new Intent(requireContext(), SubcategoryActivity.class);
            intent.putExtra("categoryId", category.id);
            startActivity(intent);
        });
        categoryGrid.setAdapter(adapter);

        view.findViewById(R.id.tv_see_all).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CategoryListActivity.class)));
    }
}
