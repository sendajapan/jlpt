package com.scholarlyapps.pathlingo.ui.utils;

import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;

public class AuthUiState {

    private final boolean loading;
    private final String error;
    private final AppUserDto user;
    private final boolean success;
    private final boolean otpVerified;

    public AuthUiState() {
        this(false, null, null, false, false);
    }

    public AuthUiState(boolean loading, String error, AppUserDto user, boolean success, boolean otpVerified) {
        this.loading = loading;
        this.error = error;
        this.user = user;
        this.success = success;
        this.otpVerified = otpVerified;
    }

    public boolean getLoading() { return loading; }
    public String getError() { return error; }
    public AppUserDto getUser() { return user; }
    public boolean getSuccess() { return success; }
    public boolean getOtpVerified() { return otpVerified; }
}
