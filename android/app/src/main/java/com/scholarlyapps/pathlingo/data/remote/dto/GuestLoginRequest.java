package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class GuestLoginRequest {

    @SerializedName("device_name")
    public final String deviceName;

    @SerializedName("guest_name")
    public final String guestName;

    public GuestLoginRequest(String deviceName, String guestName) {
        this.deviceName = deviceName;
        this.guestName = guestName;
    }
}
