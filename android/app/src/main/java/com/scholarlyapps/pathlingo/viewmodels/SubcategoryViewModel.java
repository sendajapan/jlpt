package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CatalogRepository;
import com.scholarlyapps.pathlingo.models.Category;

import java.util.Objects;

public class SubcategoryViewModel extends ViewModel {

    private final MutableLiveData<Long> categoryId = new MutableLiveData<>();
    public final LiveData<Category> category;

    public SubcategoryViewModel(CatalogRepository repo) {
        category = Transformations.switchMap(categoryId, repo::getCategoryById);
    }

    public void load(long id) {
        if (!Objects.equals(categoryId.getValue(), id)) {
            categoryId.setValue(id);
        }
    }
}
