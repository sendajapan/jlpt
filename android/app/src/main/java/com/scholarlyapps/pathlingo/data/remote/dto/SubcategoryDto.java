package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class SubcategoryDto {
    public long id;

    @SerializedName("vocab_category_id")
    public long vocabCategoryId;

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

    @SerializedName("icon_thumbnail_bg")
    public String iconThumbnailBg;

    @SerializedName("bg_url")
    public String bgUrl;

    @SerializedName("audio_url")
    public String audioUrl;

    @SerializedName("sort_order")
    public Integer sortOrder;

    @SerializedName("is_premium")
    public boolean isPremium;

    @SerializedName("coin_price")
    public int coinPrice;

    @SerializedName("is_locked")
    public boolean isLocked;
}
