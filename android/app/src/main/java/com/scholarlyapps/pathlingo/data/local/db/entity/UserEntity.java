package com.scholarlyapps.pathlingo.data.local.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    public long id;
    public String name;
    public String username;
    public String email;
    public String avatarUrl;
    public int currentLevel;
    public int xpPoints;
    public int currentStreak;
    public int learnedWords;
    @ColumnInfo(defaultValue = "0")
    public int coins;
}
