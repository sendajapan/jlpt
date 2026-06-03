package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class OtpVerifyRequest {

    @SerializedName("receiver_email")
    public final String receiverEmail;

    public final String code;

    public OtpVerifyRequest(String receiverEmail, String code) {
        this.receiverEmail = receiverEmail;
        this.code = code;
    }
}
