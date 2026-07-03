package com.scholarlyapps.pathlingo.models;

public class Word {

    public long id;
    public String kanji;
    public String reading;
    public String romaji;
    public String en;
    public String img;
    public String bgUrl;
    public String jlpt;
    public String type;
    public int xp;
    public String example_jp;
    public String example_romaji;
    public String example_en;
    public int mastery;
    public int maxMastery;
    public boolean correct;
    public boolean favorite;
    public int coinPrice;
    public boolean isLocked;
    public String audioUrl;
    public String audioEnUrl;
    public String sentenceAudioJpUrl;
    public String sentenceAudioEnUrl;

    public Word() {
    }

    public Word(String kanji, String reading, String romaji, String en, String img, String jlpt, String type, int xp) {
        this.kanji = kanji;
        this.reading = reading;
        this.romaji = romaji;
        this.en = en;
        this.img = img;
        this.jlpt = jlpt;
        this.type = type;
        this.xp = xp;
        this.maxMastery = 5;
    }
}
