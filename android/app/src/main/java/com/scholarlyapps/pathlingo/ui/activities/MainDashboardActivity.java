package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import coil.Coil;
import coil.request.ImageRequest;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ActivityMainDashboardBinding;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.ProgressViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainDashboardActivity extends AppCompatActivity {

    private ActivityMainDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        float density = getResources().getDisplayMetrics().density;
        float maxElevationPx = 8f * density;
        float maxTranslatePx = 10f * density;
        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float fraction = Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
            float eased = fraction * fraction * (3f - 2f * fraction);

            binding.expandedHeader.setAlpha(1f - eased);
            binding.expandedHeader.setScaleX(1f - 0.05f * eased);
            binding.expandedHeader.setScaleY(1f - 0.05f * eased);
            binding.expandedHeader.setTranslationY(-maxTranslatePx * eased);

            binding.header.setAlpha(eased);
            binding.header.setScaleX(0.95f + 0.05f * eased);
            binding.header.setScaleY(0.95f + 0.05f * eased);
            binding.header.setElevation(eased * maxElevationPx);
        });

        binding.txtExpandedDate.setText(new SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH).format(new Date()));

        ProgressViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(ProgressViewModel.class);
        viewModel.getUser().observe(this, user -> {
            if (user != null) {
                binding.txtExpandedName.setText(user.name);
                binding.txtExpandedCoins.setText(formatCoins(user.coins));
            }
        });
        viewModel.getAvatarUrl().observe(this, url -> {
            if (url == null || url.isEmpty()) return;
            ImageViewCompat.setImageTintList(binding.imgExpandedAvatar, null);
            Coil.imageLoader(this).enqueue(
                new ImageRequest.Builder(this)
                    .data(url)
                    .target(binding.imgExpandedAvatar)
                    .build()
            );
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.navHost);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        NavOptions tabOptions = new NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(R.id.homeFragment, false)
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_right)
            .setPopExitAnim(R.anim.slide_out_left)
            .build();

        NavOptions homeTabOptions = new NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_in_left)
            .setExitAnim(R.anim.slide_out_right)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build();

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int destId = item.getItemId();
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() == destId) {
                return true;
            }
            binding.bottomNav.post(() -> {
                try {
                    if (destId == R.id.homeFragment) {
                        navController.navigate(destId, null, homeTabOptions);
                    } else {
                        navController.navigate(destId, null, tabOptions);
                    }
                } catch (IllegalArgumentException ignored) {}
            });
            return true;
        });

        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            int id = destination.getId();
            boolean isTab = id == R.id.homeFragment
                || id == R.id.vocabularyFragment
                || id == R.id.progressFragment
                || id == R.id.notificationFragment
                || id == R.id.profileFragment;
            binding.bottomNav.setVisibility(isTab ? View.VISIBLE : View.GONE);

            boolean showHeader = id != R.id.profileFragment;
            binding.appBar.setVisibility(showHeader ? View.VISIBLE : View.GONE);
            if (showHeader) {
                binding.appBar.setExpanded(id == R.id.homeFragment, false);
            }
        });
    }

    private static String formatCoins(int coins) {
        if (coins >= 1_000_000) return String.format(Locale.US, "%.1fM", coins / 1_000_000f);
        if (coins >= 1_000) return String.format(Locale.US, "%.1fK", coins / 1_000f);
        return String.valueOf(coins);
    }
}
