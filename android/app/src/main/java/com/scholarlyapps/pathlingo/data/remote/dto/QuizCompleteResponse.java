package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class QuizCompleteResponse {

    @SerializedName("coins_earned")
    public int coinsEarned;

    @SerializedName("xp_earned")
    public int xpEarned;

    @SerializedName("total_coins")
    public int totalCoins;

    @SerializedName("total_xp")
    public int totalXp;
}
