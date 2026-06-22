package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class AvatarDto {
    public long id;
    public String name;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("coin_price")
    public int coinPrice;
    @SerializedName("is_owned")
    public boolean isOwned;
    @SerializedName("is_active")
    public boolean isActive;
}
