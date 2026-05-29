package com.scholarlyapps.pathlingo.data.remote.dto;

public class WrappedResponse<T> {
    public T data;

    public T getData() {
        return data;
    }
}
