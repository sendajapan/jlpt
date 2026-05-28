package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentLearningPreferencesBinding;
import com.scholarlyapps.pathlingo.viewmodels.OnboardingViewModel;

import java.util.Arrays;
import java.util.List;

public class LearningPreferencesFragment extends Fragment {

    private FragmentLearningPreferencesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLearningPreferencesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnboardingViewModel vm = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        List<View> profChips = Arrays.asList(
                binding.chipBeginner,
                binding.chipElementary,
                binding.chipIntermediate,
                binding.chipAdvanced,
                binding.chipNative
        );
        List<String> profValues = Arrays.asList("Beginner", "Elementary", "Intermediate", "Advanced", "Native-like");

        for (int i = 0; i < profChips.size(); i++) {
            final String prof = profValues.get(i);
            profChips.get(i).setOnClickListener(v -> {
                vm.proficiency = prof;
                for (View chip : profChips) chip.setBackgroundResource(R.drawable.bg_onboarding_unselected);
                v.setBackgroundResource(R.drawable.bg_onboarding_selected);
            });
        }

        int profIdx = profValues.indexOf(vm.proficiency);
        if (profIdx >= 0) profChips.get(profIdx).setBackgroundResource(R.drawable.bg_onboarding_selected);

        List<View> goalCards = Arrays.asList(
                binding.goalSchool,
                binding.goalBusiness,
                binding.goalTravel,
                binding.goalAnime,
                binding.goalExam,
                binding.goalCulture,
                binding.goalSocial,
                binding.goalGaming
        );
        List<String> goalValues = Arrays.asList("School", "Business", "Travel", "Anime", "Exam", "Culture", "Social", "Gaming");

        for (int i = 0; i < goalCards.size(); i++) {
            if (vm.learningGoals.contains(goalValues.get(i))) {
                goalCards.get(i).setBackgroundResource(R.drawable.bg_onboarding_selected);
            }
            final String goal = goalValues.get(i);
            final View card = goalCards.get(i);
            card.setOnClickListener(v -> {
                if (vm.learningGoals.contains(goal)) {
                    vm.learningGoals.remove(goal);
                    v.setBackgroundResource(R.drawable.bg_onboarding_unselected);
                } else {
                    vm.learningGoals.add(goal);
                    v.setBackgroundResource(R.drawable.bg_onboarding_selected);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
