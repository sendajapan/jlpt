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

import com.google.android.material.tabs.TabLayout;
import com.scholarlyapps.pathlingo.databinding.FragmentFavoritesBinding;
import com.scholarlyapps.pathlingo.models.Word;
import com.scholarlyapps.pathlingo.ui.adapters.WordAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.FavoritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private WordAdapter adapter;
    private List<Word> favoriteWords = new ArrayList<>();
    private List<Word> bookmarkedWords = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FavoritesViewModel viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(FavoritesViewModel.class);

        adapter = new WordAdapter(new ArrayList<>());
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(adapter);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showCurrentTab();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewModel.getFavorites().observe(getViewLifecycleOwner(), words -> {
            favoriteWords = words != null ? words : new ArrayList<>();
            showCurrentTab();
        });

        viewModel.getBookmarks().observe(getViewLifecycleOwner(), words -> {
            bookmarkedWords = words != null ? words : new ArrayList<>();
            showCurrentTab();
        });
    }

    private void showCurrentTab() {
        if (binding == null) return;
        boolean showingFavorites = binding.tabLayout.getSelectedTabPosition() == 0;
        List<Word> words = showingFavorites ? favoriteWords : bookmarkedWords;
        adapter.setData(words);
        binding.txtEmpty.setVisibility(words.isEmpty() ? View.VISIBLE : View.GONE);
        binding.txtEmpty.setText(showingFavorites ? "No favorites yet." : "No bookmarks yet.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
