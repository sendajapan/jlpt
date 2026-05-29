package com.scholarlyapps.pathlingo.ui.auth;

import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;

public class AuthUiState {

    private final boolean loading;
    private final String error;
    private final AppUserDto user;
    private final boolean success;

    public AuthUiState() {
        this(false, null, null, false);
    }

    public AuthUiState(boolean loading, String error, AppUserDto user, boolean success) {
        this.loading = loading;
        this.error = error;
        this.user = user;
        this.success = success;
    }

    public boolean getLoading() { return loading; }
    public String getError() { return error; }
    public AppUserDto getUser() { return user; }
    public boolean getSuccess() { return success; }
}
