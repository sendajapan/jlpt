package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.adapters.SubcategoryAdapter;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.Category;

public class SubcategoryFragment extends Fragment {

    private String categoryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subcategory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryId = requireArguments().getString("categoryId");
        DataManager dm = DataManager.getInstance();
        Category category = dm.getCategoryById(categoryId);

        TextView title = view.findViewById(R.id.title);
        title.setText(category.en);

        TextView wordCount = view.findViewById(R.id.word_count);
        wordCount.setText(category.count + " words");

        RecyclerView subcategoryList = view.findViewById(R.id.subcategory_list);
        subcategoryList.setLayoutManager(new LinearLayoutManager(requireContext()));
        SubcategoryAdapter adapter = new SubcategoryAdapter(requireContext(), category.subcategories, categoryId);
        adapter.setOnSubcategoryClickListener((catId, subcat) -> {
            Bundle args = new Bundle();
            args.putString("categoryId", catId);
            args.putString("subcategoryId", subcat.id);
            args.putInt("wordIndex", 0);
            Navigation.findNavController(view).navigate(R.id.action_subcategory_to_wordDetail, args);
        });
        subcategoryList.setAdapter(adapter);

        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
