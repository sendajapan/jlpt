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

        int score = 86;
        int maxScore = 100;
        int stars = 3;
        int xpEarned = 85;
        int correctCount = 4;
        int totalCount = 5;
        String time = "2:14";

        binding.txtScore.setText(score + "/" + maxScore);
        binding.progressScore.setProgress(score * 100 / maxScore);

        StringBuilder starText = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            starText.append(i < stars ? "⭐" : "☆");
        }
        binding.txtStars.setText(starText.toString());
        binding.txtXpEarned.setText("+" + xpEarned + " XP");
        binding.txtCorrect.setText(correctCount + "/" + totalCount);
        binding.txtTime.setText(time);

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            int levelProgress = user.maxXp > 0 ? (user.xp * 100 / user.maxXp) : 0;
            int remaining = user.maxXp - user.xp;
            binding.txtLevelTitle.setText("LEVEL " + user.level);
            binding.progressLevel.setProgress(levelProgress);
            binding.txtLevelXp.setText(user.xp + "/" + user.maxXp + " XP");
            binding.txtLevelRemaining.setText(remaining + " to go");
        });

        binding.btnContinue.setOnClickListener(v -> {
            NavOptions options = new NavOptions.Builder()
                .setPopUpTo(R.id.homeFragment, false)
                .setLaunchSingleTop(true)
                .build();
            Navigation.findNavController(v).navigate(R.id.action_score_to_home, null, options);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
