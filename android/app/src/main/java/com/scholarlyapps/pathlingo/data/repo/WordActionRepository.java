package com.scholarlyapps.pathlingo.data.repo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.scholarlyapps.pathlingo.data.local.db.dao.PendingActionDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.WordDao;
import com.scholarlyapps.pathlingo.data.local.db.entity.PendingActionEntity;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.WordReadResponse;
import com.scholarlyapps.pathlingo.data.sync.SyncScheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordActionRepository {

    public interface LearnedCallback {
        void onLearned(Integer totalCoins);
    }

    private final Context appContext;
    private final ApiService apiService;
    private final WordDao wordDao;
    private final PendingActionDao pendingActionDao;
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    public WordActionRepository(Context appContext, ApiService apiService, WordDao wordDao, PendingActionDao pendingActionDao) {
        this.appContext = appContext;
        this.apiService = apiService;
        this.wordDao = wordDao;
        this.pendingActionDao = pendingActionDao;
    }

    public void setFavorite(long wordId, boolean favorite) {
        dbExecutor.execute(() -> {
            wordDao.setFavorite(wordId, favorite);
            queueToggle(wordId,
                favorite ? PendingActionEntity.FAVORITE_ADD : PendingActionEntity.FAVORITE_REMOVE,
                List.of(PendingActionEntity.FAVORITE_ADD, PendingActionEntity.FAVORITE_REMOVE));
        });
    }

    public void setBookmarked(long wordId, boolean bookmarked) {
        dbExecutor.execute(() -> {
            wordDao.setBookmarked(wordId, bookmarked);
            queueToggle(wordId,
                bookmarked ? PendingActionEntity.BOOKMARK_ADD : PendingActionEntity.BOOKMARK_REMOVE,
                List.of(PendingActionEntity.BOOKMARK_ADD, PendingActionEntity.BOOKMARK_REMOVE));
        });
    }

    public void markLearned(long wordId, LearnedCallback callback) {
        dbExecutor.execute(() -> wordDao.setLearned(wordId));
        apiService.markRead(wordId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WordReadResponse> call, @NonNull Response<WordReadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onLearned(response.body().coins);
                } else {
                    queueLearned(wordId);
                    callback.onLearned(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WordReadResponse> call, @NonNull Throwable t) {
                queueLearned(wordId);
                callback.onLearned(null);
            }
        });
    }

    public void queueQuizCompletion(int score, int total) {
        dbExecutor.execute(() -> {
            PendingActionEntity action = new PendingActionEntity();
            action.type = PendingActionEntity.QUIZ_COMPLETE;
            action.score = score;
            action.total = total;
            action.createdAt = System.currentTimeMillis();
            pendingActionDao.insert(action);
            SyncScheduler.schedule(appContext);
        });
    }

    public void applyPendingActions() {
        for (PendingActionEntity action : pendingActionDao.getAllSync()) {
            switch (action.type) {
                case PendingActionEntity.FAVORITE_ADD:
                    wordDao.setFavorite(action.wordId, true);
                    break;
                case PendingActionEntity.FAVORITE_REMOVE:
                    wordDao.setFavorite(action.wordId, false);
                    break;
                case PendingActionEntity.BOOKMARK_ADD:
                    wordDao.setBookmarked(action.wordId, true);
                    break;
                case PendingActionEntity.BOOKMARK_REMOVE:
                    wordDao.setBookmarked(action.wordId, false);
                    break;
                case PendingActionEntity.LEARNED:
                    wordDao.setLearned(action.wordId);
                    break;
            }
        }
    }

    private void queueLearned(long wordId) {
        dbExecutor.execute(() -> {
            pendingActionDao.deleteByWordAndTypes(wordId, List.of(PendingActionEntity.LEARNED));
            PendingActionEntity action = new PendingActionEntity();
            action.type = PendingActionEntity.LEARNED;
            action.wordId = wordId;
            action.createdAt = System.currentTimeMillis();
            pendingActionDao.insert(action);
            SyncScheduler.schedule(appContext);
        });
    }

    private void queueToggle(long wordId, String type, List<String> collapseTypes) {
        pendingActionDao.deleteByWordAndTypes(wordId, collapseTypes);
        PendingActionEntity action = new PendingActionEntity();
        action.type = type;
        action.wordId = wordId;
        action.createdAt = System.currentTimeMillis();
        pendingActionDao.insert(action);
        SyncScheduler.schedule(appContext);
    }
}
