package com.scholarlyapps.pathlingo.data.local.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
    version = 2,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract SubcategoryDao subcategoryDao();
    public abstract WordDao wordDao();
    public abstract UserDao userDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE users ADD COLUMN avatarUrl TEXT");
        }
    };

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "pathlingo.db"
                    ).addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return instance;
    }
}
