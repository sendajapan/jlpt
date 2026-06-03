package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scholarlyapps.pathlingo.databinding.FragmentProgressBinding;
import com.scholarlyapps.pathlingo.ui.adapters.ProgressCategoryAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.ProgressViewModel;

import java.util.ArrayList;

public class ProgressFragment extends Fragment {

    private FragmentProgressBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgressViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(ProgressViewModel.class);

        binding.rvProgress.setLayoutManager(new LinearLayoutManager(requireContext()));
        ProgressCategoryAdapter adapter = new ProgressCategoryAdapter(new ArrayList<>());
        binding.rvProgress.setAdapter(adapter);

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            binding.userXpCard.setVisibility(View.VISIBLE);
            binding.txtLevelLabel.setText("Level " + user.level);
            binding.txtXpLabel.setText(user.xp + " / " + user.maxXp + " XP");
            binding.txtStreak.setText("🔥 " + user.streak);
            int xpPercent = user.maxXp > 0 ? (user.xp * 100 / user.maxXp) : 0;
            binding.progressXp.setProgress(xpPercent);
        });

        viewModel.categories.observe(getViewLifecycleOwner(), categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
