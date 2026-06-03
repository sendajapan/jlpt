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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentSubcategoryBinding;
import com.scholarlyapps.pathlingo.ui.adapters.SubcategoryAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.SubcategoryViewModel;

import coil.Coil;
import coil.request.ImageRequest;

public class SubcategoryFragment extends Fragment {

    private FragmentSubcategoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubcategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String categoryIdStr = getArguments() != null ? getArguments().getString("categoryId") : null;
        if (categoryIdStr == null) {
            Navigation.findNavController(view).popBackStack();
            return;
        }

        SubcategoryViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(SubcategoryViewModel.class);

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.rvSubcategories.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.category.observe(getViewLifecycleOwner(), category -> {
            if (category == null) {
                Navigation.findNavController(view).popBackStack();
                return;
            }

            binding.txtCategoryTitle.setText(category.jp + " · " + category.en);
            binding.txtCategoryMeta.setText(category.count + " words  ·  " + category.subcategories.size() + " chapters");

            if (!category.bg.isEmpty()) {
                Coil.imageLoader(requireContext()).enqueue(
                    new ImageRequest.Builder(requireContext()).data(category.bg).target(binding.imgHero).build());
            }
            if (!category.iconUrl.isEmpty()) {
                binding.imgCategoryIcon.setVisibility(View.VISIBLE);
                Coil.imageLoader(requireContext()).enqueue(
                    new ImageRequest.Builder(requireContext()).data(category.iconUrl).target(binding.imgCategoryIcon).build());
            }

            binding.rvSubcategories.setAdapter(new SubcategoryAdapter(category.subcategories, subcat -> {
                Bundle args = new Bundle();
                args.putString("subcategoryId", subcat.id);
                args.putString("iconUrl", subcat.iconUrl);
                args.putInt("wordIndex", 0);
                Navigation.findNavController(view).navigate(R.id.action_subcategory_to_word_detail, args);
            }));
        });

        viewModel.load(Long.parseLong(categoryIdStr));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
