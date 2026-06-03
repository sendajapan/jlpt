package com.scholarlyapps.pathlingo.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.scholarlyapps.pathlingo.data.local.db.entity.SubcategoryEntity;

import java.util.List;

@Dao
public interface SubcategoryDao {

    @Query("SELECT * FROM subcategories WHERE id = :id")
    LiveData<SubcategoryEntity> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SubcategoryEntity> subcategories);

    @Query("DELETE FROM subcategories")
    void deleteAll();
}
