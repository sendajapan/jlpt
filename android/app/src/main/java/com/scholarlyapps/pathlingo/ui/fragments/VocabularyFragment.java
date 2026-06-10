package com.scholarlyapps.pathlingo.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentVocabularyBinding;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.ui.activities.CategoryListActivity;
import com.scholarlyapps.pathlingo.ui.activities.SubcategoryActivity;
import com.scholarlyapps.pathlingo.ui.adapters.CategoryAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.CategoryListViewModel;

import java.util.ArrayList;

public class VocabularyFragment extends Fragment {

    private FragmentVocabularyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVocabularyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CategoryListViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(CategoryListViewModel.class);

        CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>(), new CategoryAdapter.OnCategoryClick() {
            @Override
            public void onClick(Category category) {
                openSubcategory(category.id);
            }

            @Override
            public void onUnlockClick(Category category) {
            }

            @Override
            public void onViewAllClick() {
                openCategoryList();
            }
        });
        binding.rvCategories.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 3));
        binding.rvCategories.setAdapter(adapter);

        binding.btnViewAllCategories.setOnClickListener(v -> openCategoryList());

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>()));

        viewModel.loadCategories();
    }

    private void openSubcategory(String categoryId) {
        Intent intent = new Intent(requireContext(), SubcategoryActivity.class);
        intent.putExtra(SubcategoryActivity.EXTRA_CATEGORY_ID, categoryId);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openCategoryList() {
        Intent intent = new Intent(requireContext(), CategoryListActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
