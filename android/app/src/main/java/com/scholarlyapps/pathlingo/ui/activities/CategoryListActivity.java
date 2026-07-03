package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.databinding.ActivityCategoryListBinding;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.ui.UnlockBottomSheet;
import com.scholarlyapps.pathlingo.ui.adapters.CategoryAdapter;
import com.scholarlyapps.pathlingo.ui.decorations.GridSpacingItemDecoration;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.CategoryListViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends AppCompatActivity {

    private ActivityCategoryListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.scrollView.setPadding(0, 0, 0, bottom);
            return insets;
        });

        binding.toolbar.setOnBackClickListener(v -> {
            finish();
            NavAnim.slideBack(this);
        });

        CategoryListViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(CategoryListViewModel.class);

        CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>(), true, new CategoryAdapter.OnCategoryClick() {
            @Override
            public void onClick(Category category) {
                Intent intent = new Intent(CategoryListActivity.this, SubcategoryActivity.class);
                intent.putExtra(SubcategoryActivity.EXTRA_CATEGORY_ID, category.id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onUnlockClick(Category category) {
                fetchCoinsAndUnlock(category);
            }

            @Override
            public void onViewAllClick() {
            }
        });

        int spacing = (int) (8 * getResources().getDisplayMetrics().density);
        binding.rvCategories.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 3));
        binding.rvCategories.addItemDecoration(new GridSpacingItemDecoration(3, spacing));
        binding.rvCategories.setAdapter(adapter);

        viewModel.getCategories().observe(this, categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>()));

        viewModel.loadCategories();
    }

    private void fetchCoinsAndUnlock(Category category) {
        ServiceLocator.api.coinsBalance().enqueue(new Callback<CoinsBalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoinsBalanceResponse> call, @NonNull Response<CoinsBalanceResponse> response) {
                if (isFinishing()) return;
                int coins = response.isSuccessful() && response.body() != null ? response.body().coins : 0;
                showUnlockSheet(category, coins);
            }

            @Override
            public void onFailure(@NonNull Call<CoinsBalanceResponse> call, @NonNull Throwable t) {
                if (isFinishing()) return;
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
        sheet.show(getSupportFragmentManager(), "unlock");
    }

    @Override
    public void onBackPressed() {
        NavAnim.slideBack(this);
        super.onBackPressed();
    }
}
