package com.scholarlyapps.pathlingo.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SplashViewModel extends ViewModel {

    public enum Destination { DASHBOARD, LOGIN }

    private final MutableLiveData<Destination> destination = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public LiveData<Destination> getDestination() {
        return destination;
    }

    public void checkAuth() {
        executor.execute(() -> {
            String token = ServiceLocator.authRepository.storedToken();
            Destination dest = Destination.LOGIN;

            if (token != null && !token.isEmpty()) {
                boolean valid = ServiceLocator.userRepository.refresh();
                if (valid) {
                    dest = Destination.DASHBOARD;
                } else {
                    ServiceLocator.tokenStore.clear();
                }
            }

            Destination finalDest = dest;
            mainHandler.post(() -> destination.setValue(finalDest));
        });
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
    }
}
