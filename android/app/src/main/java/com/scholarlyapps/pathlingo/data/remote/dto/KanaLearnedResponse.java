package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class KanaLearnedResponse {
    public boolean learned;

    @SerializedName("first_time")
    public boolean firstTime;

    public int coins;
}
