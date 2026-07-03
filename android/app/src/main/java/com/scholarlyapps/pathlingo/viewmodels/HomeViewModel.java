package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CategoryRepository;
import com.scholarlyapps.pathlingo.data.repo.UserRepository;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {

    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MediatorLiveData<List<Category>> categories = new MediatorLiveData<>();
    private final MediatorLiveData<User> user = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> refreshError = new MutableLiveData<>();

    private boolean loaded = false;

    public HomeViewModel(CategoryRepository categoryRepo, UserRepository userRepo) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getRefreshError() {
        return refreshError;
    }

    public void clearRefreshError() {
        refreshError.setValue(null);
    }

    public void loadData() {
        if (loaded) return;
        loaded = true;
        loading.setValue(true);

        categories.addSource(categoryRepo.getAllCategories(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                categories.setValue(categoryList);
            }
        });

        user.addSource(userRepo.getUser(), user::setValue);

        executor.execute(() -> {
            categoryRepo.refresh(refreshError::postValue);
            userRepo.refresh();
            loading.postValue(false);
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
