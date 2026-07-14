package com.scholarlyapps.pathlingo.data.repo;

import androidx.annotation.NonNull;

import com.scholarlyapps.pathlingo.data.networking.ApiErrors;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.KanaDto;
import com.scholarlyapps.pathlingo.data.remote.dto.KanaLearnedResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.ListResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KanaRepository {

    public interface Listener<T> {
        void onSuccess(T data);

        void onError(String message);
    }

    private final ApiService apiService;
    private final Map<String, List<KanaDto>> kanaCache = new HashMap<>();

    public KanaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void getKanas(String script, Listener<List<KanaDto>> listener) {
        List<KanaDto> cached = kanaCache.get(script);
        if (cached != null) {
            listener.onSuccess(cached);
            return;
        }

        apiService.getKanas(script).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListResponse<KanaDto>> call, @NonNull Response<ListResponse<KanaDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to load letters."));
                    return;
                }
                kanaCache.put(script, response.body().getData());
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(@NonNull Call<ListResponse<KanaDto>> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }

    public void getKana(long kanaId, Listener<KanaDto> listener) {
        apiService.getKana(kanaId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<WrappedResponse<KanaDto>> call, @NonNull Response<WrappedResponse<KanaDto>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to load letter."));
                    return;
                }
                listener.onSuccess(response.body().getData());
            }

            @Override
            public void onFailure(@NonNull Call<WrappedResponse<KanaDto>> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }

    public void markLearned(long kanaId, Listener<KanaLearnedResponse> listener) {
        apiService.markKanaLearned(kanaId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<KanaLearnedResponse> call, @NonNull Response<KanaLearnedResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    listener.onError(ApiErrors.message(response, "Failed to save progress."));
                    return;
                }
                markLearnedInCache(kanaId);
                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<KanaLearnedResponse> call, @NonNull Throwable throwable) {
                listener.onError(ApiErrors.message(throwable));
            }
        });
    }

    private void markLearnedInCache(long kanaId) {
        for (List<KanaDto> kanas : kanaCache.values()) {
            for (KanaDto kana : kanas) {
                if (kana.id == kanaId) {
                    kana.isLearned = true;
                    return;
                }
            }
        }
    }
}
