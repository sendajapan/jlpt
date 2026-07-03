package com.scholarlyapps.pathlingo.data.local.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pending_actions")
public class PendingActionEntity {

    public static final String FAVORITE_ADD = "favorite_add";
    public static final String FAVORITE_REMOVE = "favorite_remove";
    public static final String BOOKMARK_ADD = "bookmark_add";
    public static final String BOOKMARK_REMOVE = "bookmark_remove";
    public static final String LEARNED = "learned";
    public static final String QUIZ_COMPLETE = "quiz_complete";

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String type;
    @ColumnInfo(defaultValue = "0")
    public long wordId;
    @ColumnInfo(defaultValue = "0")
    public int score;
    @ColumnInfo(defaultValue = "0")
    public int total;
    @ColumnInfo(defaultValue = "0")
    public long createdAt;
}
