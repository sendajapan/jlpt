package com.scholarlyapps.pathlingo.data.repo;

import androidx.annotation.NonNull;

import com.scholarlyapps.pathlingo.data.networking.ApiErrors;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.KanaLearnedResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.KanjiDto;
import com.scholarlyapps.pathlingo.data.remote.dto.KanjiStrokeGroupDto;
import com.scholarlyapps.pathlingo.data.remote.dto.ListResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.PagedResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KanjiRepository {

    public interface Listener<T> {
        void onSuccess(T data);

        void onError(String message);
    }

    private final ApiService apiService;

    public KanjiRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getKanjis(String jlpt, Integer strokes, int page, int perPage, Listener<PagedResponse<KanjiDto>> listener) {
        apiService.getKanjis(jlpt, strokes, page, perPage).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PagedResponse<KanjiDto>> call, @NonNull Response<PagedResponse<KanjiDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to load kanji."));
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PagedResponse<KanjiDto>> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }

    public void getStrokeGroups(Listener<List<KanjiStrokeGroupDto>> listener) {
        apiService.getKanjiStrokeGroups().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListResponse<KanjiStrokeGroupDto>> call, @NonNull Response<ListResponse<KanjiStrokeGroupDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to load stroke counts."));
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(@NonNull Call<ListResponse<KanjiStrokeGroupDto>> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }

    public void getKanji(long kanjiId, Listener<KanjiDto> listener) {
        apiService.getKanji(kanjiId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WrappedResponse<KanjiDto>> call, @NonNull Response<WrappedResponse<KanjiDto>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to load kanji."));
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(@NonNull Call<WrappedResponse<KanjiDto>> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }

    public void markLearned(long kanjiId, Listener<KanaLearnedResponse> listener) {
        apiService.markKanjiLearned(kanjiId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<KanaLearnedResponse> call, @NonNull Response<KanaLearnedResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to save progress."));
                    return;
                }
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<KanaLearnedResponse> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }

    public void learnedIds(Listener<List<Long>> listener) {
        apiService.myKanjiLearned().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListResponse<Long>> call, @NonNull Response<ListResponse<Long>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to load progress."));
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(@NonNull Call<ListResponse<Long>> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }
}
