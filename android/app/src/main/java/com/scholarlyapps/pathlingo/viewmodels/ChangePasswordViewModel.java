package com.scholarlyapps.pathlingo.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.repo.AuthRepository;
import com.scholarlyapps.pathlingo.data.repo.UserRepository;
import com.scholarlyapps.pathlingo.models.User;
import com.scholarlyapps.pathlingo.ui.activities.profile.ChangePasswordUiState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChangePasswordViewModel extends ViewModel {

    private final AuthRepository authRepo;
    private final UserRepository userRepo;
    private final MutableLiveData<ChangePasswordUiState> stateLiveData = new MutableLiveData<>(new ChangePasswordUiState());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ChangePasswordViewModel(AuthRepository authRepo, UserRepository userRepo) {
        this.authRepo = authRepo;
        this.userRepo = userRepo;
    }

    public LiveData<ChangePasswordUiState> getState() {
        return stateLiveData;
    }

    public LiveData<User> getUser() {
        return userRepo.getUser();
    }

    public void sendResetLink(String email) {
        if (email.isEmpty()) {
            stateLiveData.setValue(new ChangePasswordUiState(false, "Email is required.", false));
            return;
        }
        stateLiveData.setValue(new ChangePasswordUiState(true, null, false));
        executor.execute(() -> {
            ApiResult<String> result = authRepo.forgotPassword(email);
            ChangePasswordUiState next = result.isSuccess()
                    ? new ChangePasswordUiState(false, null, true)
                    : new ChangePasswordUiState(false, result.getError(), false);
            mainHandler.post(() -> stateLiveData.setValue(next));
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
