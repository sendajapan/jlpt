package com.scholarlyapps.pathlingo.ui.auth;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashViewModel extends ViewModel {

    private final MutableLiveData<Boolean> authenticated = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LiveData<Boolean> getAuthenticated() {
        return authenticated;
    }

    public void checkAuth() {
        executor.execute(() -> {
            String token = ServiceLocator.authRepository.storedToken();
            boolean isAuthenticated = false;
            if (token != null && !token.isEmpty()) {
                ApiResult<AppUserDto> me = ServiceLocator.userRepository.me();
                if (me.isSuccess()) {
                    isAuthenticated = true;
                } else {
                    ServiceLocator.tokenStore.clear();
                }
            }
            boolean finalResult = isAuthenticated;
            mainHandler.post(() -> authenticated.setValue(finalResult));
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
