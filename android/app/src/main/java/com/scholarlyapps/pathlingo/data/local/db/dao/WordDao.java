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

    @Query("SELECT * FROM words WHERE isBookmarked = 1")
    LiveData<List<WordEntity>> getBookmarks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WordEntity> words);

    @Query("DELETE FROM words")
    void deleteAll();

    @Query("UPDATE words SET isFavorite = :isFavorite WHERE id = :wordId")
    void setFavorite(long wordId, boolean isFavorite);

    @Query("UPDATE words SET isBookmarked = :isBookmarked WHERE id = :wordId")
    void setBookmarked(long wordId, boolean isBookmarked);

    @Query("UPDATE words SET isLearned = 1 WHERE id = :wordId")
    void setLearned(long wordId);

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT :limit")
    List<WordEntity> getRandomWordsSync(int limit);
}
