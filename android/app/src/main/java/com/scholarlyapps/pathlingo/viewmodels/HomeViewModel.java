package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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

    private final MediatorLiveData<List<Category>> _categories = new MediatorLiveData<>();
    public final LiveData<List<Category>> categories = _categories;

    private final MediatorLiveData<User> _user = new MediatorLiveData<>();
    public final LiveData<User> user = _user;

    private boolean sourcesAdded = false;

    public HomeViewModel(CatalogRepository catalogRepo, UserRepository userRepo) {
        this.catalogRepo = catalogRepo;
        this.userRepo = userRepo;
    }

    public void loadData() {
        if (!sourcesAdded) {
            sourcesAdded = true;
            _categories.addSource(catalogRepo.getAllCategories(), _categories::setValue);
            _user.addSource(userRepo.getUser(), _user::setValue);
            executor.execute(() -> {
                catalogRepo.refresh();
                userRepo.refresh();
            });
        }
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
