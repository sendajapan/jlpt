package com.scholarlyapps.pathlingo.ui.activities.profile;

public class ChangePasswordUiState {

    private final boolean loading;
    private final String error;
    private final boolean success;

    public ChangePasswordUiState() {
        this(false, null, false);
    }

    public ChangePasswordUiState(boolean loading, String error, boolean success) {
        this.loading = loading;
        this.error = error;
        this.success = success;
    }

    public boolean getLoading() { return loading; }
    public String getError() { return error; }
    public boolean getSuccess() { return success; }
}
