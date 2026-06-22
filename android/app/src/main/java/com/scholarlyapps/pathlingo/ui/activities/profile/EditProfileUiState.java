package com.scholarlyapps.pathlingo.ui.activities.profile;

import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;

public class EditProfileUiState {

    private final boolean loading;
    private final String error;
    private final boolean success;
    private final AppUserDto profile;

    public EditProfileUiState() {
        this(false, null, false, null);
    }

    public EditProfileUiState(boolean loading, String error, boolean success, AppUserDto profile) {
        this.loading = loading;
        this.error = error;
        this.success = success;
        this.profile = profile;
    }

    public boolean getLoading() {
        return loading;
    }

    public String getError() {
        return error;
    }

    public boolean getSuccess() {
        return success;
    }

    public AppUserDto getProfile() {
        return profile;
    }
}
