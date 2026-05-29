package com.scholarlyapps.pathlingo.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStore {

    private static final String PREF_NAME = "pathlingo_auth";
    private static final String KEY_TOKEN = "sanctum_token";

    private final SharedPreferences prefs;

    public TokenStore(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void save(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public void clear() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }
}
