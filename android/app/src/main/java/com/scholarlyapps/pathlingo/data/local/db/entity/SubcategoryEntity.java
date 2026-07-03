package com.scholarlyapps.pathlingo.data.local.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subcategories")
public class SubcategoryEntity {
    @PrimaryKey
    public long id;
    public long categoryId;
    public String nameJp;
    public String nameEn;
    public String nameRomaji;
    public String iconUrl;
    public String iconThumbnailUrl;
    public String iconThumbnailBg;
    public String bgUrl;
    public boolean isPremium;
    @ColumnInfo(defaultValue = "0")
    public int coinPrice;
    @ColumnInfo(defaultValue = "0")
    public boolean isLocked;
}
