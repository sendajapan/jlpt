package com.scholarlyapps.pathlingo.data;

public class ApiResult<T> {

    private final T data;
    private final String error;

    private ApiResult(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(data, null);
    }

    public static <T> ApiResult<T> failure(String error) {
        return new ApiResult<>(null, error != null ? error : "Unknown error");
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
