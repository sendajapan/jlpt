package com.scholarlyapps.pathlingo.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.scholarlyapps.pathlingo.data.local.db.entity.PendingActionEntity;

import java.util.List;

@Dao
public interface PendingActionDao {

    @Insert
    void insert(PendingActionEntity action);

    @Query("SELECT * FROM pending_actions ORDER BY createdAt ASC")
    List<PendingActionEntity> getAllSync();

    @Query("DELETE FROM pending_actions WHERE id = :id")
    void delete(long id);

    @Query("DELETE FROM pending_actions WHERE wordId = :wordId AND type IN (:types)")
    void deleteByWordAndTypes(long wordId, List<String> types);
}
