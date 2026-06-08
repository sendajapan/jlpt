package com.scholarlyapps.pathlingo.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentHomeBinding;
import com.scholarlyapps.pathlingo.ui.adapters.CategoryAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.HomeViewModel;

import java.util.ArrayList;

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

        binding.btnSeeAll.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.vocabularyFragment));

        binding.btnStartLesson.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.vocabularyFragment));

        CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>(), new CategoryAdapter.OnCategoryClick() {
            @Override
            public void onClick(com.scholarlyapps.pathlingo.models.Category category) {
                Bundle args = new Bundle();
                args.putString("categoryId", category.id);
                Navigation.findNavController(view).navigate(R.id.action_home_to_subcategory, args);
            }

            @Override
            public void onUnlockClick(com.scholarlyapps.pathlingo.models.Category category) {
            }
        });

        binding.rvCategories.setAdapter(adapter);

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

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>())
        );

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.loadingCategories.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.rvCategories.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        new Handler(Looper.getMainLooper()).post(viewModel::loadData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
