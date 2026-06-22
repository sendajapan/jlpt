package com.scholarlyapps.pathlingo.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.scholarlyapps.pathlingo.data.local.db.entity.CategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.relation.CategoryWithChildren;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories")
    LiveData<List<CategoryEntity>> getAll();

    @Transaction
    @Query("SELECT * FROM categories")
    LiveData<List<CategoryWithChildren>> getAllWithChildren();

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :id")
    LiveData<CategoryWithChildren> getWithChildrenById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categories);

    @Query("DELETE FROM categories")
    void deleteAll();
}
