package com.scholarlyapps.pathlingo.data.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.scholarlyapps.pathlingo.data.local.db.dao.PendingActionDao;
import com.scholarlyapps.pathlingo.data.local.db.entity.PendingActionEntity;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizCompleteRequest;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class PendingActionSyncWorker extends Worker {

    public PendingActionSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        ServiceLocator.init(getApplicationContext());
        ApiService api = ServiceLocator.api;
        PendingActionDao dao = ServiceLocator.db.pendingActionDao();

        List<PendingActionEntity> actions = dao.getAllSync();
        boolean syncedAny = false;

        for (PendingActionEntity action : actions) {
            try {
                Response<?> response = execute(api, action);
                if (response.isSuccessful() || response.code() == 404 || response.code() == 422) {
                    dao.delete(action.id);
                    syncedAny = true;
                } else if (response.code() == 401) {
                    dao.delete(action.id);
                } else {
                    return Result.retry();
                }
            } catch (IOException e) {
                return Result.retry();
            }
        }

        if (syncedAny) {
            ServiceLocator.userRepository.refresh();
        }
        return Result.success();
    }

    private Response<?> execute(ApiService api, PendingActionEntity action) throws IOException {
        switch (action.type) {
            case PendingActionEntity.FAVORITE_ADD:
                return api.addFavorite(action.wordId).execute();
            case PendingActionEntity.FAVORITE_REMOVE:
                return api.removeFavorite(action.wordId).execute();
            case PendingActionEntity.BOOKMARK_ADD:
                return api.addBookmark(action.wordId).execute();
            case PendingActionEntity.BOOKMARK_REMOVE:
                return api.removeBookmark(action.wordId).execute();
            case PendingActionEntity.LEARNED:
                return api.markRead(action.wordId).execute();
            case PendingActionEntity.QUIZ_COMPLETE:
                return api.completeQuiz(new QuizCompleteRequest(action.score, action.total)).execute();
            default:
                return api.me().execute();
        }
    }
}
