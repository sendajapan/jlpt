package com.scholarlyapps.pathlingo.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {
    @PrimaryKey
    public long id;
    public long subcategoryId;
    public String wordJp;
    public String wordRomaji;
    public String wordEn;
    public String sentenceJp;
    public String sentenceRomaji;
    public String sentenceEn;
    public String audioJpUrl;
    public String audioEnUrl;
    public String sentenceAudioJpUrl;
    public String sentenceAudioEnUrl;
    public String imageUrl;
    public boolean isPremium;
    public int coinPrice;
    public boolean isLocked;
    public boolean isFavorite;
}
