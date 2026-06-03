package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentCategoryListBinding;
import com.scholarlyapps.pathlingo.ui.adapters.CategoryAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.CategoryListViewModel;

import java.util.ArrayList;

public class CategoryListFragment extends Fragment {

    private FragmentCategoryListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CategoryListViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(CategoryListViewModel.class);

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>(), category -> {
            Bundle args = new Bundle();
            args.putString("categoryId", category.id);
            Navigation.findNavController(view).navigate(R.id.action_categories_to_subcategory, args);
        });
        binding.rvCategories.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2));
        binding.rvCategories.setAdapter(adapter);

        viewModel.categories.observe(getViewLifecycleOwner(), categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
