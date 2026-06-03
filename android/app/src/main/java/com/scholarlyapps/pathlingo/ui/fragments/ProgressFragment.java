package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.databinding.FragmentProgressBinding;
import com.scholarlyapps.pathlingo.models.User;
import com.scholarlyapps.pathlingo.ui.adapters.ProgressCategoryAdapter;

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
        DataManager dm = DataManager.getInstance();
        User user = dm.getUser();

        if (user != null) {
            binding.userXpCard.setVisibility(View.VISIBLE);
            binding.txtLevelLabel.setText("Level " + user.level);
            binding.txtXpLabel.setText(user.xp + " / " + user.maxXp + " XP");
            binding.txtStreak.setText("🔥 " + user.streak);
            int xpPercent = user.maxXp > 0 ? (user.xp * 100 / user.maxXp) : 0;
            binding.progressXp.setProgress(xpPercent);
        }

        binding.rvProgress.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvProgress.setAdapter(new ProgressCategoryAdapter(dm.getCategories()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
