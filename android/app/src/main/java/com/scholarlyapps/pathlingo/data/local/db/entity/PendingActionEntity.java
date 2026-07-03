package com.scholarlyapps.pathlingo.data.local.db.entity;

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
    public long wordId;
    public int score;
    public int total;
    public long createdAt;
}
