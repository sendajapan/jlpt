package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class VocabularyDto {
    public long id;

    @SerializedName("vocab_subcategory_id")
    public long vocabSubcategoryId;

    @SerializedName("word_jp")
    public String wordJp;

    @SerializedName("word_romaji")
    public String wordRomaji;

    @SerializedName("word_en")
    public String wordEn;

    @SerializedName("sentence_jp")
    public String sentenceJp;

    @SerializedName("sentence_romaji")
    public String sentenceRomaji;

    @SerializedName("sentence_en")
    public String sentenceEn;

    @SerializedName("audio_jp_url")
    public String audioJpUrl;

    @SerializedName("audio_en_url")
    public String audioEnUrl;

    @SerializedName("sentence_audio_jp_url")
    public String sentenceAudioJpUrl;

    @SerializedName("sentence_audio_en_url")
    public String sentenceAudioEnUrl;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("image_thumbnail_url")
    public String imageThumbnailUrl;

    @SerializedName("image_thumbnail_bg")
    public String imageThumbnailBg;

    @SerializedName("bg_url")
    public String bgUrl;

    @SerializedName("sort_order")
    public Integer sortOrder;

    @SerializedName("is_premium")
    public boolean isPremium;

    @SerializedName("coin_price")
    public int coinPrice;

    @SerializedName("is_locked")
    public boolean isLocked;

    @SerializedName("is_favorite")
    public boolean isFavorite;

    @SerializedName("is_bookmarked")
    public boolean isBookmarked;

    @SerializedName("is_learned")
    public boolean isLearned;
}
