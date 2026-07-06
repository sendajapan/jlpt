package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class KanaWordDto {
    public long id;

    @SerializedName("word_jp")
    public String wordJp;

    @SerializedName("word_romaji")
    public String wordRomaji;

    @SerializedName("word_en")
    public String wordEn;

    @SerializedName("audio_jp_url")
    public String audioJpUrl;

    @SerializedName("image_thumbnail_url")
    public String imageThumbnailUrl;

    @SerializedName("image_thumbnail_bg")
    public String imageThumbnailBg;
}
