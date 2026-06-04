package com.scholarlyapps.pathlingo.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.repo.UserRepository;
import com.scholarlyapps.pathlingo.ui.activities.profile.EditProfileUiState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditProfileViewModel extends ViewModel {

    private final UserRepository userRepo;
    private final MutableLiveData<EditProfileUiState> stateLiveData = new MutableLiveData<>(new EditProfileUiState());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public EditProfileViewModel(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public LiveData<EditProfileUiState> getState() {
        return stateLiveData;
    }

    public void refreshProfile() {
        executor.execute(userRepo::refresh);
    }

    public void loadProfile() {
        stateLiveData.setValue(new EditProfileUiState(true, null, false, null));
        executor.execute(() -> {
            ApiResult<AppUserDto> result = userRepo.fetchProfile();
            EditProfileUiState next = result.isSuccess()
                    ? new EditProfileUiState(false, null, false, result.getData())
                    : new EditProfileUiState(false, result.getError(), false, null);
            mainHandler.post(() -> stateLiveData.setValue(next));
        });
    }

    public void save(String name, String username, String bio, String gender, String birthDate,
                     String proficiencyLevel, String learningGoal, String dailyGoalMinutes) {
        if (name.isEmpty()) {
            stateLiveData.setValue(new EditProfileUiState(false, "Name is required.", false, null));
            return;
        }
        stateLiveData.setValue(new EditProfileUiState(true, null, false, null));
        executor.execute(() -> {
            ApiResult<AppUserDto> result = userRepo.updateProfile(name, username, bio, gender, birthDate,
                    proficiencyLevel, learningGoal, dailyGoalMinutes);
            EditProfileUiState next = result.isSuccess()
                    ? new EditProfileUiState(false, null, true, result.getData())
                    : new EditProfileUiState(false, result.getError(), false, null);
            mainHandler.post(() -> stateLiveData.setValue(next));
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
