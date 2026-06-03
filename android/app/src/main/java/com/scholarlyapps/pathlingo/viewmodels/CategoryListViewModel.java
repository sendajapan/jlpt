package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CatalogRepository;
import com.scholarlyapps.pathlingo.models.Category;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryListViewModel extends ViewModel {

    private final CatalogRepository repo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final LiveData<List<Category>> categories;

    public CategoryListViewModel(CatalogRepository repo) {
        this.repo = repo;
        this.categories = repo.getAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void loadCategories() {
        executor.execute(repo::refresh);
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
