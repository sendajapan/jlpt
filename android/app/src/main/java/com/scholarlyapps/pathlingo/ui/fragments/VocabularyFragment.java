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
import com.scholarlyapps.pathlingo.databinding.FragmentVocabularyBinding;
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
            public void onClick(com.scholarlyapps.pathlingo.models.Category category) {
                Bundle args = new Bundle();
                args.putString("categoryId", category.id);
                Navigation.findNavController(view).navigate(R.id.action_vocabulary_to_subcategory, args);
            }

            @Override
            public void onUnlockClick(com.scholarlyapps.pathlingo.models.Category category) {
            }
        });
        binding.rvCategories.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 3));
        binding.rvCategories.setAdapter(adapter);

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>()));

        viewModel.loadCategories();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
