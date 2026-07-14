package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KanjiDto {
    public long id;

    public String kanji;

    public int strokes;

    public Integer grade;

    public Integer freq;

    public String jlpt;

    public String translate;

    public String meanings;

    @SerializedName("readings_on")
    public String readingsOn;

    @SerializedName("readings_kun")
    public String readingsKun;

    public Integer level;

    public String radicals;

    @SerializedName("vocab_id")
    public Long vocabId;

    @SerializedName("is_premium")
    public boolean isPremium;

    @SerializedName("is_learned")
    public boolean isLearned;

    @SerializedName("example_words")
    public List<KanaWordDto> exampleWords;
}
