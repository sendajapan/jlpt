package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ActivityMainDashboardBinding;

import java.util.Objects;

public class MainDashboardActivity extends AppCompatActivity {

    private ActivityMainDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                || id == R.id.favoritesFragment
                || id == R.id.progressFragment
                || id == R.id.profileFragment;
            binding.bottomNav.setVisibility(isTab ? View.VISIBLE : View.GONE);
        });
    }
}
