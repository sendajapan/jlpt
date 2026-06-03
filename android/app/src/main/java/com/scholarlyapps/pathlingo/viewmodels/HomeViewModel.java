package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CatalogRepository;
import com.scholarlyapps.pathlingo.data.repo.UserRepository;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {

    private final CatalogRepository catalogRepo;
    private final UserRepository userRepo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public final LiveData<User> user;
    public final LiveData<List<Category>> categories;

    public HomeViewModel(CatalogRepository catalogRepo, UserRepository userRepo) {
        this.catalogRepo = catalogRepo;
        this.userRepo = userRepo;
        this.user = userRepo.getUser();
        this.categories = catalogRepo.getAllCategories();
    }

    public void refresh() {
        executor.execute(() -> {
            catalogRepo.refresh();
            userRepo.refresh();
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
