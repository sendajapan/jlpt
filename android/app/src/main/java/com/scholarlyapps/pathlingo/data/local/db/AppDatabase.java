package com.scholarlyapps.pathlingo.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.scholarlyapps.pathlingo.data.local.db.dao.CategoryDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.SubcategoryDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.UserDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.WordDao;
import com.scholarlyapps.pathlingo.data.local.db.entity.CategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.SubcategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.UserEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.WordEntity;

@Database(
    entities = {CategoryEntity.class, SubcategoryEntity.class, WordEntity.class, UserEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract SubcategoryDao subcategoryDao();
    public abstract WordDao wordDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "pathlingo.db"
                    ).build();
                }
            }
        }
        return instance;
    }
}
