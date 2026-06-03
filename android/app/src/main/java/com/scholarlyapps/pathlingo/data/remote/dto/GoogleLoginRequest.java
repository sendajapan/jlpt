package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class GoogleLoginRequest {

    @SerializedName("id_token")
    public final String idToken;

    @SerializedName("device_name")
    public final String deviceName;

    public GoogleLoginRequest(String idToken, String deviceName) {
        this.idToken = idToken;
        this.deviceName = deviceName;
    }
}
