package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ActivityMainDashboardBinding;

import java.util.Objects;

public class MainDashboardActivity extends AppCompatActivity {

    private ActivityMainDashboardBinding binding;
    private int statusBarInset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            statusBarInset = bars.top;

            int dp16 = (int) (16 * getResources().getDisplayMetrics().density);
            ViewGroup.MarginLayoutParams headerParams = (ViewGroup.MarginLayoutParams) binding.header.getLayoutParams();
            headerParams.topMargin = bars.top + dp16;
            binding.header.requestLayout();

            binding.bottomNav.setPadding(0, 0, 0, bars.bottom);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.navHost);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        NavOptions tabOptions = new NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(R.id.homeFragment, false)
            .setEnterAnim(0)
            .setExitAnim(0)
            .setPopEnterAnim(0)
            .setPopExitAnim(0)
            .build();

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int destId = item.getItemId();
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() == destId) {
                return true;
            }
            binding.bottomNav.post(() -> {
                try {
                    navController.navigate(destId, null, tabOptions);
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
            binding.navHost.setPadding(0, showHeader ? 0 : statusBarInset, 0, 0);
        });
    }
}
