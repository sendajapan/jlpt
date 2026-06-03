package com.scholarlyapps.pathlingo.viewmodels;

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

    public enum Destination { DASHBOARD, OTP_VERIFY, LOGIN }

    private final MutableLiveData<Destination> destination = new MutableLiveData<>();
    private final MutableLiveData<String> pendingEmail = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LiveData<Destination> getDestination() {
        return destination;
    }

    public LiveData<String> getPendingEmail() {
        return pendingEmail;
    }

    public void checkAuth() {
        executor.execute(() -> {
            String token = ServiceLocator.authRepository.storedToken();
            Destination dest = Destination.LOGIN;
            String email = null;

            if (token != null && !token.isEmpty()) {
                ApiResult<AppUserDto> me = ServiceLocator.userRepository.me();
                if (me.isSuccess() && me.getData() != null) {
                    AppUserDto user = me.getData();
                    if (user.emailVerified) {
                        dest = Destination.DASHBOARD;
                    } else {
                        dest = Destination.OTP_VERIFY;
                        email = user.email;
                    }
                } else {
                    ServiceLocator.tokenStore.clear();
                }
            }

            Destination finalDest = dest;
            String finalEmail = email;
            mainHandler.post(() -> {
                pendingEmail.setValue(finalEmail);
                destination.setValue(finalDest);
            });
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
