package com.scholarlyapps.pathlingo.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.scholarlyapps.pathlingo.data.local.db.entity.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<UserEntity> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    @Query("DELETE FROM users")
    void deleteAll();
}
