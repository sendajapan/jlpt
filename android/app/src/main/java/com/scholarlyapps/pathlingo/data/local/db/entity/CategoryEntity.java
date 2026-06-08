package com.scholarlyapps.pathlingo.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {
    @PrimaryKey
    public long id;
    public String nameJp;
    public String nameEn;
    public String nameRomaji;
    public String iconUrl;
    public String iconThumbnailUrl;
    public String bgUrl;
    public boolean isPremium;
    public int coinPrice;
    public boolean isLocked;
}
