package com.scholarlyapps.pathlingo.data.repo;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.ListResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.MessageResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Response;

public class CatalogRepository {

    private final ApiService api;

    public CatalogRepository(ApiService api) {
        this.api = api;
    }

    public ApiResult<CatalogData> loadAll() {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            Future<List<CategoryDto>> catFuture = pool.submit(() -> {
                Response<ListResponse<CategoryDto>> r = api.getCategories().execute();
                return r.isSuccessful() && r.body() != null ? r.body().getData() : Collections.emptyList();
            });
            Future<List<SubcategoryDto>> subFuture = pool.submit(() -> {
                Response<ListResponse<SubcategoryDto>> r = api.getSubcategories(null).execute();
                return r.isSuccessful() && r.body() != null ? r.body().getData() : Collections.emptyList();
            });
            Future<List<VocabularyDto>> vocabFuture = pool.submit(() -> {
                Response<ListResponse<VocabularyDto>> r = api.getVocabularies(null, null, null).execute();
                return r.isSuccessful() && r.body() != null ? r.body().getData() : Collections.emptyList();
            });
            return ApiResult.success(new CatalogData(catFuture.get(), subFuture.get(), vocabFuture.get()));
        } catch (Exception e) {
            return ApiResult.failure(e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    public ApiResult<MessageResponse> addFavorite(long vocabularyId) {
        try {
            Response<MessageResponse> r = api.addFavorite(vocabularyId).execute();
            return r.isSuccessful() && r.body() != null
                ? ApiResult.success(r.body())
                : ApiResult.failure("Failed: " + r.code());
        } catch (Exception e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<MessageResponse> removeFavorite(long vocabularyId) {
        try {
            Response<MessageResponse> r = api.removeFavorite(vocabularyId).execute();
            return r.isSuccessful() && r.body() != null
                ? ApiResult.success(r.body())
                : ApiResult.failure("Failed: " + r.code());
        } catch (Exception e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<Void> markRead(long vocabularyId) {
        try {
            api.markRead(vocabularyId).execute();
            return ApiResult.success(null);
        } catch (Exception e) {
            return ApiResult.failure(e.getMessage());
        }
    }
}
