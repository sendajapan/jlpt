package com.scholarlyapps.pathlingo.data.repo;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse;

import java.io.IOException;

import retrofit2.Response;

public class UserRepository {

    private final ApiService api;

    public UserRepository(ApiService api) {
        this.api = api;
    }

    public ApiResult<AppUserDto> me() {
        try {
            Response<WrappedResponse<AppUserDto>> response = api.me().execute();
            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                return ApiResult.success(response.body().getData());
            }
            return ApiResult.failure("Failed: " + response.code());
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<Integer> coinsBalance() {
        try {
            Response<CoinsBalanceResponse> response = api.coinsBalance().execute();
            if (response.isSuccessful() && response.body() != null) {
                return ApiResult.success(response.body().coins);
            }
            return ApiResult.failure("Failed: " + response.code());
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }
}
