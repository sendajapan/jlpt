package com.scholarlyapps.pathlingo.ui.auth;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.repo.AuthRepository;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthViewModel extends ViewModel {

    private final AuthRepository repo = ServiceLocator.authRepository;
    private final MutableLiveData<AuthUiState> stateLiveData = new MutableLiveData<>(new AuthUiState());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LiveData<AuthUiState> getStateLiveData() {
        return stateLiveData;
    }

    public void login(String email, String password, String deviceName) {
        launchAuth(() -> repo.login(email, password, deviceName));
    }

    public void register(String name, String email, String password, String confirm, String deviceName) {
        launchAuth(() -> repo.register(name, email, password, confirm, deviceName));
    }

    public void guestLogin(String deviceName) {
        launchAuth(() -> repo.guestLogin(deviceName));
    }

    public void loginWithGoogle(String idToken, String deviceName) {
        launchAuth(() -> repo.loginWithGoogle(idToken, deviceName));
    }

    private void launchAuth(Callable<ApiResult<AppUserDto>> block) {
        stateLiveData.setValue(new AuthUiState(true, null, null, false));
        executor.execute(() -> {
            AuthUiState next;
            try {
                ApiResult<AppUserDto> result = block.call();
                next = result.isSuccess()
                        ? new AuthUiState(false, null, result.getData(), true)
                        : new AuthUiState(false, result.getError(), null, false);
            } catch (Exception e) {
                next = new AuthUiState(false, e.getMessage() != null ? e.getMessage() : "Something went wrong.", null, false);
            }
            AuthUiState finalNext = next;
            mainHandler.post(() -> stateLiveData.setValue(finalNext));
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
