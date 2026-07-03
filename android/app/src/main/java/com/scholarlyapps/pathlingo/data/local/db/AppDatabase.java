package com.scholarlyapps.pathlingo.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.scholarlyapps.pathlingo.data.local.db.dao.CategoryDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.PendingActionDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.SubcategoryDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.UserDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.WordDao;
import com.scholarlyapps.pathlingo.data.local.db.entity.CategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.PendingActionEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.SubcategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.UserEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.WordEntity;

@Database(
    entities = {CategoryEntity.class, SubcategoryEntity.class, WordEntity.class, UserEntity.class, PendingActionEntity.class},
    version = 9,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract SubcategoryDao subcategoryDao();
    public abstract WordDao wordDao();
    public abstract UserDao userDao();
    public abstract PendingActionDao pendingActionDao();

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

    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE words ADD COLUMN isBookmarked INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE words ADD COLUMN isLearned INTEGER NOT NULL DEFAULT 0");
            db.execSQL("CREATE TABLE IF NOT EXISTS pending_actions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "type TEXT, " +
                "wordId INTEGER NOT NULL DEFAULT 0, " +
                "score INTEGER NOT NULL DEFAULT 0, " +
                "total INTEGER NOT NULL DEFAULT 0, " +
                "createdAt INTEGER NOT NULL DEFAULT 0)");
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            addColumnIfMissing(db, "categories", "coinPrice INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "categories", "isLocked INTEGER NOT NULL DEFAULT 0");

            addColumnIfMissing(db, "subcategories", "coinPrice INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "subcategories", "isLocked INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "subcategories", "bgUrl TEXT");

            addColumnIfMissing(db, "words", "coinPrice INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "words", "isLocked INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "words", "audioEnUrl TEXT");
            addColumnIfMissing(db, "words", "sentenceAudioJpUrl TEXT");
            addColumnIfMissing(db, "words", "sentenceAudioEnUrl TEXT");
            addColumnIfMissing(db, "words", "bgUrl TEXT");
            addColumnIfMissing(db, "words", "isBookmarked INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "words", "isLearned INTEGER NOT NULL DEFAULT 0");

            addColumnIfMissing(db, "users", "avatarUrl TEXT");
            addColumnIfMissing(db, "users", "coins INTEGER NOT NULL DEFAULT 0");

            db.execSQL("CREATE TABLE IF NOT EXISTS pending_actions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "type TEXT, " +
                "wordId INTEGER NOT NULL DEFAULT 0, " +
                "score INTEGER NOT NULL DEFAULT 0, " +
                "total INTEGER NOT NULL DEFAULT 0, " +
                "createdAt INTEGER NOT NULL DEFAULT 0)");
            addColumnIfMissing(db, "pending_actions", "wordId INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "pending_actions", "score INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "pending_actions", "total INTEGER NOT NULL DEFAULT 0");
            addColumnIfMissing(db, "pending_actions", "createdAt INTEGER NOT NULL DEFAULT 0");

            db.execSQL("CREATE TABLE IF NOT EXISTS categories_new (" +
                "id INTEGER NOT NULL, " +
                "nameJp TEXT, nameEn TEXT, nameRomaji TEXT, " +
                "iconUrl TEXT, iconThumbnailUrl TEXT, bgUrl TEXT, " +
                "isPremium INTEGER NOT NULL, " +
                "coinPrice INTEGER NOT NULL DEFAULT 0, " +
                "isLocked INTEGER NOT NULL DEFAULT 0, " +
                "PRIMARY KEY(id))");
            db.execSQL("INSERT INTO categories_new (id, nameJp, nameEn, nameRomaji, iconUrl, iconThumbnailUrl, bgUrl, isPremium, coinPrice, isLocked) " +
                "SELECT id, nameJp, nameEn, nameRomaji, iconUrl, iconThumbnailUrl, bgUrl, isPremium, coinPrice, isLocked FROM categories");
            db.execSQL("DROP TABLE categories");
            db.execSQL("ALTER TABLE categories_new RENAME TO categories");

            db.execSQL("CREATE TABLE IF NOT EXISTS subcategories_new (" +
                "id INTEGER NOT NULL, " +
                "categoryId INTEGER NOT NULL, " +
                "nameJp TEXT, nameEn TEXT, nameRomaji TEXT, " +
                "iconUrl TEXT, iconThumbnailUrl TEXT, iconThumbnailBg TEXT, bgUrl TEXT, " +
                "isPremium INTEGER NOT NULL, " +
                "coinPrice INTEGER NOT NULL DEFAULT 0, " +
                "isLocked INTEGER NOT NULL DEFAULT 0, " +
                "PRIMARY KEY(id))");
            db.execSQL("INSERT INTO subcategories_new (id, categoryId, nameJp, nameEn, nameRomaji, iconUrl, iconThumbnailUrl, iconThumbnailBg, bgUrl, isPremium, coinPrice, isLocked) " +
                "SELECT id, categoryId, nameJp, nameEn, nameRomaji, iconUrl, iconThumbnailUrl, iconThumbnailBg, bgUrl, isPremium, coinPrice, isLocked FROM subcategories");
            db.execSQL("DROP TABLE subcategories");
            db.execSQL("ALTER TABLE subcategories_new RENAME TO subcategories");

            db.execSQL("CREATE TABLE IF NOT EXISTS words_new (" +
                "id INTEGER NOT NULL, " +
                "subcategoryId INTEGER NOT NULL, " +
                "wordJp TEXT, wordRomaji TEXT, wordEn TEXT, " +
                "sentenceJp TEXT, sentenceRomaji TEXT, sentenceEn TEXT, " +
                "audioJpUrl TEXT, audioEnUrl TEXT, sentenceAudioJpUrl TEXT, sentenceAudioEnUrl TEXT, " +
                "imageUrl TEXT, bgUrl TEXT, " +
                "isPremium INTEGER NOT NULL, " +
                "coinPrice INTEGER NOT NULL DEFAULT 0, " +
                "isLocked INTEGER NOT NULL DEFAULT 0, " +
                "isFavorite INTEGER NOT NULL, " +
                "isBookmarked INTEGER NOT NULL DEFAULT 0, " +
                "isLearned INTEGER NOT NULL DEFAULT 0, " +
                "PRIMARY KEY(id))");
            db.execSQL("INSERT INTO words_new (id, subcategoryId, wordJp, wordRomaji, wordEn, sentenceJp, sentenceRomaji, sentenceEn, " +
                "audioJpUrl, audioEnUrl, sentenceAudioJpUrl, sentenceAudioEnUrl, imageUrl, bgUrl, isPremium, coinPrice, isLocked, isFavorite, isBookmarked, isLearned) " +
                "SELECT id, subcategoryId, wordJp, wordRomaji, wordEn, sentenceJp, sentenceRomaji, sentenceEn, " +
                "audioJpUrl, audioEnUrl, sentenceAudioJpUrl, sentenceAudioEnUrl, imageUrl, bgUrl, isPremium, coinPrice, isLocked, isFavorite, isBookmarked, isLearned FROM words");
            db.execSQL("DROP TABLE words");
            db.execSQL("ALTER TABLE words_new RENAME TO words");

            db.execSQL("CREATE TABLE IF NOT EXISTS users_new (" +
                "id INTEGER NOT NULL, " +
                "name TEXT, username TEXT, email TEXT, avatarUrl TEXT, " +
                "currentLevel INTEGER NOT NULL, " +
                "xpPoints INTEGER NOT NULL, " +
                "currentStreak INTEGER NOT NULL, " +
                "learnedWords INTEGER NOT NULL, " +
                "coins INTEGER NOT NULL DEFAULT 0, " +
                "PRIMARY KEY(id))");
            db.execSQL("INSERT INTO users_new (id, name, username, email, avatarUrl, currentLevel, xpPoints, currentStreak, learnedWords, coins) " +
                "SELECT id, name, username, email, avatarUrl, currentLevel, xpPoints, currentStreak, learnedWords, coins FROM users");
            db.execSQL("DROP TABLE users");
            db.execSQL("ALTER TABLE users_new RENAME TO users");

            db.execSQL("CREATE TABLE IF NOT EXISTS pending_actions_new (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "type TEXT, " +
                "wordId INTEGER NOT NULL DEFAULT 0, " +
                "score INTEGER NOT NULL DEFAULT 0, " +
                "total INTEGER NOT NULL DEFAULT 0, " +
                "createdAt INTEGER NOT NULL DEFAULT 0)");
            db.execSQL("INSERT INTO pending_actions_new (id, type, wordId, score, total, createdAt) " +
                "SELECT id, type, wordId, score, total, createdAt FROM pending_actions");
            db.execSQL("DROP TABLE pending_actions");
            db.execSQL("ALTER TABLE pending_actions_new RENAME TO pending_actions");
        }
    };

    private static void addColumnIfMissing(SupportSQLiteDatabase db, String table, String columnDef) {
        try {
            db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + columnDef);
        } catch (SQLiteException e) {
            if (e.getMessage() == null || !e.getMessage().contains("duplicate column name")) {
                throw e;
            }
        }
    }

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "pathlingo.db"
                    ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }
}
