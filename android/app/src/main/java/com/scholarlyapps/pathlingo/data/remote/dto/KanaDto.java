package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KanaDto {
    public long id;

    public String script;

    public String kana;

    public String romaji;

    @SerializedName("sound_url")
    public String soundUrl;

    public String section;

    public int position;

    @SerializedName("is_learned")
    public boolean isLearned;

    @SerializedName("example_words")
    public List<KanaWordDto> exampleWords;
}
