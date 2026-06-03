package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class OtpSendRequest {

    @SerializedName("receiver_email")
    public final String receiverEmail;

    public OtpSendRequest(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}
