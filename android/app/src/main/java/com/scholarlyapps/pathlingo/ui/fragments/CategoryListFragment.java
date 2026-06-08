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
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.databinding.FragmentCategoryListBinding;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.ui.UnlockBottomSheet;
import com.scholarlyapps.pathlingo.ui.adapters.CategoryAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.CategoryListViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>(), new CategoryAdapter.OnCategoryClick() {
            @Override
            public void onClick(Category category) {
                Bundle args = new Bundle();
                args.putString("categoryId", category.id);
                Navigation.findNavController(view).navigate(R.id.action_vocabulary_to_subcategory, args);
            }

            @Override
            public void onUnlockClick(Category category) {
                fetchCoinsAndUnlock(category);
            }
        });
        binding.rvCategories.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2));
        binding.rvCategories.setAdapter(adapter);

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>()));

        viewModel.loadCategories();
    }

    private void fetchCoinsAndUnlock(Category category) {
        ServiceLocator.api.coinsBalance().enqueue(new Callback<CoinsBalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoinsBalanceResponse> call, @NonNull Response<CoinsBalanceResponse> response) {
                if (!isAdded()) return;
                int coins = response.isSuccessful() && response.body() != null ? response.body().coins : 0;
                showUnlockSheet(category, coins);
            }

            @Override
            public void onFailure(@NonNull Call<CoinsBalanceResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                showUnlockSheet(category, 0);
            }
        });
    }

    private void showUnlockSheet(Category category, int userCoins) {
        UnlockBottomSheet sheet = UnlockBottomSheet.create(
                UnlockBottomSheet.Type.CATEGORY,
                Long.parseLong(category.id),
                category.jp + " · " + category.en,
                category.coinPrice,
                userCoins
        );
        sheet.setOnUnlockListener(() -> ServiceLocator.categoryRepository.refresh());
        sheet.show(getChildFragmentManager(), "unlock");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
