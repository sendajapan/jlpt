package com.scholarlyapps.pathlingo.data.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkMonitor {

    private final ConnectivityManager connectivityManager;
    private final MutableLiveData<Boolean> online = new MutableLiveData<>();

    public NetworkMonitor(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        online.postValue(isOnline());
        connectivityManager.registerNetworkCallback(
            new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    online.postValue(true);
                }

                @Override
                public void onLost(Network network) {
                    online.postValue(isOnline());
                }
            }
        );
    }

    public LiveData<Boolean> getOnline() {
        return online;
    }

    public boolean isOnline() {
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
