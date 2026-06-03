package com.scholarlyapps.pathlingo.ui.utils;

import android.content.Context;
import android.content.Intent;

import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.ui.auth.LoginActivity;

import java.util.concurrent.Executors;

public class LogoutHelper {

    public static void logout(Context context) {
        ServiceLocator.init(context);
        Executors.newSingleThreadExecutor().execute(() -> ServiceLocator.authRepository.logout());
        Intent intent = new Intent(context, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
