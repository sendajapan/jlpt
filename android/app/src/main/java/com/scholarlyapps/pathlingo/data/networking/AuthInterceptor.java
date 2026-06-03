package com.scholarlyapps.pathlingo.data.networking;

import com.scholarlyapps.pathlingo.data.local.TokenStore;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenStore tokenStore;

    public AuthInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder()
            .header("Accept", "application/json");

        if (!isPublicAuthEndpoint(request.url().encodedPath())) {
            String token = tokenStore.getToken();
            if (token != null && !token.isEmpty()) {
                builder.header("Authorization", "Bearer " + token);
            }
        }

        return chain.proceed(builder.build());
    }

    private boolean isPublicAuthEndpoint(String path) {
        return path.endsWith("/auth/login") ||
            path.endsWith("/auth/register") ||
            path.endsWith("/auth/guest") ||
            path.contains("/auth/social/") ||
            path.endsWith("/auth/forgot-password") ||
            path.endsWith("/auth/reset-password");
    }
}
