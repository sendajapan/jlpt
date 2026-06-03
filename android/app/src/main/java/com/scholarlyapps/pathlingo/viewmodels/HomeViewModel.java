package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public HomeViewModel(CatalogRepository catalogRepo, UserRepository userRepo) {
        this.catalogRepo = catalogRepo;
        this.userRepo = userRepo;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void loadData() {
        catalogRepo.getAllCategories().observeForever(categories::postValue);
        userRepo.getUser().observeForever(user::postValue);
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
