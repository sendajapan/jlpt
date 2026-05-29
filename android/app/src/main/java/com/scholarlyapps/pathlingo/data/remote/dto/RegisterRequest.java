package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    public final String name;
    public final String email;
    public final String password;

    @SerializedName("password_confirmation")
    public final String passwordConfirmation;

    @SerializedName("device_name")
    public final String deviceName;

    @SerializedName("display_name")
    public final String displayName;

    @SerializedName("guest_token")
    public final String guestToken;

    public RegisterRequest(String name, String email, String password, String passwordConfirmation, String deviceName, String guestToken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.deviceName = deviceName;
        this.displayName = null;
        this.guestToken = guestToken;
    }
}
