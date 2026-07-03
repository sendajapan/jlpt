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
    version = 8,
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

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE categories ADD COLUMN coinPrice INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE categories ADD COLUMN isLocked INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE subcategories ADD COLUMN coinPrice INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE subcategories ADD COLUMN isLocked INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE words ADD COLUMN coinPrice INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE words ADD COLUMN isLocked INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE users ADD COLUMN coins INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE subcategories ADD COLUMN bgUrl TEXT");
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE words ADD COLUMN audioEnUrl TEXT");
            db.execSQL("ALTER TABLE words ADD COLUMN sentenceAudioJpUrl TEXT");
            db.execSQL("ALTER TABLE words ADD COLUMN sentenceAudioEnUrl TEXT");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE words ADD COLUMN bgUrl TEXT");
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
                    ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }
}
