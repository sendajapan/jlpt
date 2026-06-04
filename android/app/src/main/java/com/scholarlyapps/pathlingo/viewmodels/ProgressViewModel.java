package com.scholarlyapps.pathlingo.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.repo.CategoryRepository;
import com.scholarlyapps.pathlingo.data.repo.UserRepository;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgressViewModel extends ViewModel {

    private final LiveData<User> user;
    private final LiveData<List<Category>> categories;
    private final UserRepository userRepo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final MutableLiveData<String> avatarUrl = new MutableLiveData<>();

    public ProgressViewModel(CategoryRepository catalogRepo, UserRepository userRepo) {
        this.userRepo = userRepo;
        this.user = userRepo.getUser();
        this.categories = catalogRepo.getAllCategories();
    }

    public void refresh() {
        executor.execute(() -> {
            AppUserDto dto = userRepo.fetchAndSave();
            if (dto != null) {
                String url = dto.avatarUrl != null ? dto.avatarUrl : dto.avatar;
                mainHandler.post(() -> avatarUrl.setValue(url));
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<String> getAvatarUrl() {
        return avatarUrl;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
