package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CategoryRepository;
import com.scholarlyapps.pathlingo.models.Category;

import java.util.Objects;

public class SubcategoryViewModel extends ViewModel {

    private final MutableLiveData<Long> categoryId = new MutableLiveData<>();
    private final LiveData<Category> category;

    public SubcategoryViewModel(CategoryRepository repo) {
        category = Transformations.switchMap(categoryId, repo::getCategoryById);
    }

    public LiveData<Category> getCategory() {
        return category;
    }

    public void load(long id) {
        if (!Objects.equals(categoryId.getValue(), id)) {
            categoryId.setValue(id);
        }
    }
}
