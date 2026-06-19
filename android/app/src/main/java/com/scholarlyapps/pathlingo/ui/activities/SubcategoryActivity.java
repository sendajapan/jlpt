package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.databinding.ActivitySubcategoryBinding;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.ui.UnlockBottomSheet;
import com.scholarlyapps.pathlingo.ui.adapters.SubcategoryAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.SubcategoryViewModel;

import coil.Coil;
import coil.request.ImageRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubcategoryActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "categoryId";

    private ActivitySubcategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubcategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.rvSubcategories.setPadding(
                binding.rvSubcategories.getPaddingLeft(),
                binding.rvSubcategories.getPaddingTop(),
                binding.rvSubcategories.getPaddingRight(),
                bottom
            );
            return insets;
        });

        String categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);
        if (categoryId == null) {
            finish();
            return;
        }

        binding.toolbar.setOnBackClickListener(v -> finish());
        binding.rvSubcategories.setLayoutManager(new LinearLayoutManager(this));

        SubcategoryViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(SubcategoryViewModel.class);

        viewModel.getCategory().observe(this, category -> {
            if (category == null) {
                finish();
                return;
            }

            binding.rvSubcategories.setAdapter(new SubcategoryAdapter(category.subcategories, new SubcategoryAdapter.OnSubcategoryClick() {
                @Override
                public void onClick(Subcategory subcat) {
                    openWordDetail(subcat);
                }

                @Override
                public void onUnlockClick(Subcategory subcat) {
                    fetchCoinsAndUnlock(subcat);
                }
            }));
        });

        viewModel.load(Long.parseLong(categoryId));
    }

    private void openWordDetail(Subcategory subcat) {
        Intent intent = new Intent(this, WordDetailActivity.class);
        intent.putExtra(WordDetailActivity.EXTRA_SUBCATEGORY_ID, subcat.id);
        intent.putExtra(WordDetailActivity.EXTRA_ICON_URL, subcat.iconUrl != null ? subcat.iconUrl : "");
        intent.putExtra(WordDetailActivity.EXTRA_WORD_INDEX, 0);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void fetchCoinsAndUnlock(Subcategory subcat) {
        ServiceLocator.api.coinsBalance().enqueue(new Callback<CoinsBalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CoinsBalanceResponse> call, @NonNull Response<CoinsBalanceResponse> response) {
                if (isFinishing()) return;
                int coins = response.isSuccessful() && response.body() != null ? response.body().coins : 0;
                showUnlockSheet(subcat, coins);
            }

            @Override
            public void onFailure(@NonNull Call<CoinsBalanceResponse> call, @NonNull Throwable t) {
                if (isFinishing()) return;
                showUnlockSheet(subcat, 0);
            }
        });
    }

    private void showUnlockSheet(Subcategory subcat, int userCoins) {
        UnlockBottomSheet sheet = UnlockBottomSheet.create(
            UnlockBottomSheet.Type.SUBCATEGORY,
            Long.parseLong(subcat.id),
            subcat.jp + " · " + subcat.en,
            subcat.coinPrice,
            userCoins
        );
        sheet.setOnUnlockListener(() -> ServiceLocator.categoryRepository.refresh());
        sheet.show(getSupportFragmentManager(), "unlock");
    }
}
