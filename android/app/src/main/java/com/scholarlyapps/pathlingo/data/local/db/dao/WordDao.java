package com.scholarlyapps.pathlingo.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.scholarlyapps.pathlingo.data.local.db.entity.WordEntity;

import java.util.List;

@Dao
public interface WordDao {

    @Query("SELECT * FROM words WHERE subcategoryId = :subcategoryId")
    LiveData<List<WordEntity>> getBySubcategoryId(long subcategoryId);

    @Query("SELECT * FROM words WHERE isFavorite = 1")
    LiveData<List<WordEntity>> getFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WordEntity> words);

    @Query("DELETE FROM words")
    void deleteAll();

    @Query("UPDATE words SET isFavorite = :isFavorite WHERE id = :wordId")
    void setFavorite(long wordId, boolean isFavorite);

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT :limit")
    List<WordEntity> getRandomWordsSync(int limit);
}
