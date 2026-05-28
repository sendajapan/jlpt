package com.scholarlyapps.pathlingo.data.networking;

import com.google.gson.Gson;
import com.scholarlyapps.pathlingo.BuildConfig;
import com.scholarlyapps.pathlingo.data.local.TokenStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static ApiService create(TokenStore tokenStore) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG
            ? HttpLoggingInterceptor.Level.BODY
            : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(new AuthInterceptor(tokenStore))
            .addInterceptor(logging)
            .build();

        return new Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(new Gson()))
            .build()
            .create(ApiService.class);
    }
}
