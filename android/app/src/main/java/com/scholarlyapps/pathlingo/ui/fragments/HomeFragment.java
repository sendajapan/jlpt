package com.scholarlyapps.pathlingo.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentHomeBinding;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        HomeViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(HomeViewModel.class);

        binding.btnStartQuiz.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.vocabularyFragment));

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

            int progress = user.maxXp > 0 ? (user.xp * 100 / user.maxXp) : 0;
            binding.progressLesson.setProgress(progress);
            binding.txtLessonPercent.setText(progress + "% complete");
            binding.txtLessonFraction.setText(user.streak + "/7");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
