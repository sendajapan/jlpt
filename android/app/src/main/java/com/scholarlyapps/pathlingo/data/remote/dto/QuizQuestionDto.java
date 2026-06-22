package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizQuestionDto {

    public String type;

    public WordDto word;

    @SerializedName("correct_answer")
    public String correctAnswer;

    public List<String> options;

    public static class WordDto {
        public long id;

        @SerializedName("word_jp")
        public String wordJp;

        @SerializedName("word_romaji")
        public String wordRomaji;

        @SerializedName("word_en")
        public String wordEn;

        @SerializedName("image_url")
        public String imageUrl;

        @SerializedName("audio_jp_url")
        public String audioJpUrl;

        @SerializedName("audio_en_url")
        public String audioEnUrl;

        @SerializedName("sentence_audio_jp_url")
        public String sentenceAudioJpUrl;

        @SerializedName("sentence_audio_en_url")
        public String sentenceAudioEnUrl;
    }
}
