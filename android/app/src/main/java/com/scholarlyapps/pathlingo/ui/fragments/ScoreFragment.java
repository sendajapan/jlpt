package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentScoreBinding;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.ProgressViewModel;

public class ScoreFragment extends Fragment {

    private FragmentScoreBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgressViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(ProgressViewModel.class);

        Bundle args = getArguments();
        int correctCount = args != null ? args.getInt("score", 0) : 0;
        int totalCount = args != null ? args.getInt("total", 10) : 10;
        int scorePercent = totalCount > 0 ? (correctCount * 100 / totalCount) : 0;
        int xpEarned = correctCount * 10;
        int stars = scorePercent >= 90 ? 3 : scorePercent >= 60 ? 2 : scorePercent >= 30 ? 1 : 0;

        binding.txtScore.setText(scorePercent + "/100");
        binding.progressScore.setProgress(scorePercent);

        StringBuilder starText = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            starText.append(i < stars ? "⭐" : "☆");
        }
        binding.txtStars.setText(starText.toString());
        binding.txtXpEarned.setText("+" + xpEarned + " XP");
        binding.txtCorrect.setText(correctCount + "/" + totalCount);
        binding.txtTime.setText("--");

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            int levelProgress = user.maxXp > 0 ? (user.xp * 100 / user.maxXp) : 0;
            int remaining = user.maxXp - user.xp;
            binding.txtLevelTitle.setText("LEVEL " + user.level);
            binding.progressLevel.setProgress(levelProgress);
            binding.txtLevelXp.setText(user.xp + "/" + user.maxXp + " XP");
            binding.txtLevelRemaining.setText(remaining + " to go");
        });

        binding.btnContinue.setOnClickListener(v -> {
            try {
                NavOptions options = new NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, false)
                    .setLaunchSingleTop(true)
                    .build();
                Navigation.findNavController(v).navigate(R.id.action_score_to_home, null, options);
            } catch (IllegalArgumentException e) {
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
