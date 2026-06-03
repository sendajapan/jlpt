package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CatalogRepository;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.List;
import java.util.Objects;

public class WordDetailViewModel extends ViewModel {

    private final MutableLiveData<Long> subcategoryId = new MutableLiveData<>();
    public final LiveData<List<Word>> words;

    public WordDetailViewModel(CatalogRepository repo) {
        words = Transformations.switchMap(subcategoryId, repo::getWordsForSubcategory);
    }

    public void load(long id) {
        if (!Objects.equals(subcategoryId.getValue(), id)) {
            subcategoryId.setValue(id);
        }
    }
}
