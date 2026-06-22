package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CategoryRepository;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.List;

public class FavoritesViewModel extends ViewModel {

    private final LiveData<List<Word>> favorites;

    public FavoritesViewModel(CategoryRepository repo) {
        favorites = repo.getFavoriteWords();
    }

    public LiveData<List<Word>> getFavorites() {
        return favorites;
    }
}
