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
import com.scholarlyapps.pathlingo.databinding.FragmentFavoritesBinding;
import com.scholarlyapps.pathlingo.models.Word;
import com.scholarlyapps.pathlingo.ui.adapters.WordAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<Word> favorites = new ArrayList<>();
        DataManager.getInstance().getCategories().forEach(cat ->
                cat.subcategories.forEach(sub ->
                        sub.words.stream().filter(w -> w.favorite).forEach(favorites::add)));

        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(new WordAdapter(favorites));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
