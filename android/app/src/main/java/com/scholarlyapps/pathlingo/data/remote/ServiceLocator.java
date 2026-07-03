package com.scholarlyapps.pathlingo.data.remote;

import android.content.Context;

import com.scholarlyapps.pathlingo.data.local.TokenStore;
import com.scholarlyapps.pathlingo.data.local.db.AppDatabase;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.networking.RetrofitClient;
import com.scholarlyapps.pathlingo.data.networking.NetworkMonitor;
import com.scholarlyapps.pathlingo.data.repo.AuthRepository;
import com.scholarlyapps.pathlingo.data.repo.CategoryRepository;
import com.scholarlyapps.pathlingo.data.repo.UserRepository;
import com.scholarlyapps.pathlingo.data.repo.WordActionRepository;

public class ServiceLocator {

    private static volatile boolean initialized = false;

    public static TokenStore tokenStore;
    public static ApiService api;
    public static AppDatabase db;
    public static AuthRepository authRepository;
    public static CategoryRepository categoryRepository;
    public static UserRepository userRepository;
    public static WordActionRepository wordActionRepository;
    public static NetworkMonitor networkMonitor;

    public static void init(Context context) {
        if (initialized) return;
        synchronized (ServiceLocator.class) {
            if (initialized) return;
            Context appContext = context.getApplicationContext();
            tokenStore = new TokenStore(appContext);
            api = RetrofitClient.create(tokenStore);
            db = AppDatabase.getInstance(appContext);
            networkMonitor = new NetworkMonitor(appContext);
            userRepository = new UserRepository(api, db.userDao());
            authRepository = new AuthRepository(api, tokenStore, userRepository);
            wordActionRepository = new WordActionRepository(appContext, api, db.wordDao(), db.pendingActionDao());
            categoryRepository = new CategoryRepository(api, db.categoryDao(), db.subcategoryDao(), db.wordDao(), wordActionRepository);
            initialized = true;
        }
    }
}
