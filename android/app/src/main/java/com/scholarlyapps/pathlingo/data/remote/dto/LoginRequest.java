package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    public final String email;
    public final String password;

    @SerializedName("device_name")
    public final String deviceName;

    @SerializedName("guest_token")
    public final String guestToken;

    public LoginRequest(String email, String password, String deviceName, String guestToken) {
        this.email = email;
        this.password = password;
        this.deviceName = deviceName;
        this.guestToken = guestToken;
    }
}
