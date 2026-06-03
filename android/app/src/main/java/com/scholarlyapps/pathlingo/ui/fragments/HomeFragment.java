package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        binding.txtDate.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(new Date()));

        binding.btnSeeAll.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_home_to_categories));

        binding.btnStartLesson.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_home_to_categories));

        CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>(), category -> {
            Bundle args = new Bundle();
            args.putString("categoryId", category.id);
            Navigation.findNavController(view).navigate(R.id.action_home_to_subcategory, args);
        });
        binding.rvCategories.setAdapter(adapter);

        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            binding.txtUserName.setText("Ready to learn, " + user.name + "!");
            binding.txtCoins.setText(String.valueOf(user.xp));
            binding.txtStreak.setText(String.valueOf(user.streak));
            binding.txtWordsKnown.setText(String.valueOf(user.wordsKnown));
            binding.txtXpDisplay.setText(String.valueOf(user.xp));

            int progress = user.maxXp > 0 ? (user.xp * 100 / user.maxXp) : 0;
            binding.progressLesson.setProgress(progress);
            binding.txtLessonPercent.setText(progress + "% complete");
            binding.txtLessonFraction.setText(user.streak + "/7");
        });

        viewModel.categories.observe(getViewLifecycleOwner(), categories ->
            adapter.setData(categories != null ? categories : new ArrayList<>()));

        viewModel.refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
