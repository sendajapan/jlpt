package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentHomeBinding;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.HomeViewModel;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        HomeViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(HomeViewModel.class);

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        binding.menuVocabulary.setOnClickListener(v -> bottomNav.setSelectedItemId(R.id.vocabularyFragment));
        binding.menuGrammar.setOnClickListener(v -> bottomNav.setSelectedItemId(R.id.vocabularyFragment));
        binding.menuKatakana.setOnClickListener(v -> bottomNav.setSelectedItemId(R.id.vocabularyFragment));
        binding.menuHiragana.setOnClickListener(v -> bottomNav.setSelectedItemId(R.id.vocabularyFragment));
        binding.menuReading.setOnClickListener(v -> bottomNav.setSelectedItemId(R.id.vocabularyFragment));
        binding.menuKanji.setOnClickListener(v -> bottomNav.setSelectedItemId(R.id.vocabularyFragment));

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            binding.txtStreak.setText(String.valueOf(user.streak));
            binding.txtWordsKnown.setText(String.valueOf(user.wordsKnown));
            binding.txtXpDisplay.setText(String.valueOf(user.xp));
            updateStreakCard(user.streak);
        });

        viewModel.loadData();
    }

    private void updateStreakCard(int streak) {
        binding.txtStreakCount.setText(String.valueOf(streak));

        int todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        int cappedStreak = Math.min(streak, 7);

        int[] circleIds = {
            R.id.day0Circle, R.id.day1Circle, R.id.day2Circle, R.id.day3Circle,
            R.id.day4Circle, R.id.day5Circle, R.id.day6Circle
        };

        for (int i = 0; i < 7; i++) {
            View circle = binding.getRoot().findViewById(circleIds[i]);
            if (i > todayIndex) {
                circle.setBackgroundResource(R.drawable.bg_streak_day_pending);
            } else if (i == todayIndex) {
                circle.setBackgroundResource(cappedStreak > 0
                    ? R.drawable.bg_streak_day_today
                    : R.drawable.bg_streak_day_pending);
            } else {
                int daysAgo = todayIndex - i;
                circle.setBackgroundResource(daysAgo < cappedStreak
                    ? R.drawable.bg_streak_day_done
                    : R.drawable.bg_streak_day_pending);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
