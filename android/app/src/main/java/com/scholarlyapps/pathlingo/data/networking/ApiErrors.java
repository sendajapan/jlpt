package com.scholarlyapps.pathlingo.data.networking;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ApiErrors {

    public static String message(Response<?> response, String fallback) {
        if (response.code() == 401) return "Session expired. Please log in again.";
        if (response.code() >= 500) return "Server error. Please try again later.";
        try {
            ResponseBody errorBody = response.errorBody();
            if (errorBody == null) return fallback;
            JSONObject json = new JSONObject(errorBody.string());
            if (json.has("errors")) {
                JSONObject errors = json.getJSONObject("errors");
                String firstKey = errors.keys().next();
                return errors.getJSONArray(firstKey).getString(0);
            }
            if (json.has("message")) return json.getString("message");
        } catch (Exception ignored) {
        }
        return fallback;
    }

    public static String message(Throwable t) {
        if (t instanceof SocketTimeoutException) return "Request timed out. Please try again.";
        if (t instanceof IOException) return "No internet connection. Please check your network.";
        return "Something went wrong. Please try again.";
    }
}
