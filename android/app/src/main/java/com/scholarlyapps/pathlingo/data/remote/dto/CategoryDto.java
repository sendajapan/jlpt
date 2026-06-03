package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class CategoryDto {
    public long id;
    @SerializedName("name_en")
    public String nameEn;
    @SerializedName("name_jp")
    public String nameJp;
    @SerializedName("name_romaji")
    public String nameRomaji;
    @SerializedName("icon_url")
    public String iconUrl;
    @SerializedName("icon_thumbnail_url")
    public String iconThumbnailUrl;
    @SerializedName("category_bg_path")
    public String categoryBgPath;
    @SerializedName("bg_url")
    public String bgUrl;
    @SerializedName("audio_url")
    public String audioUrl;
    @SerializedName("sort_order")
    public Integer sortOrder;
    @SerializedName("is_premium")
    public boolean isPremium;
}
