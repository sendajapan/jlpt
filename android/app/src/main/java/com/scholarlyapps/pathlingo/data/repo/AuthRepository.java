package com.scholarlyapps.pathlingo.data.repo;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.local.TokenStore;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.remote.dto.AuthResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.ForgotPasswordRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.GoogleLoginRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.GuestLoginRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.LoginRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.MessageResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.OtpSendRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.OtpVerifyRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.RegisterRequest;

import java.io.IOException;

import retrofit2.Response;

public class AuthRepository {

    private final ApiService api;
    private final TokenStore tokenStore;
    private final UserRepository userRepository;

    public AuthRepository(ApiService api, TokenStore tokenStore, UserRepository userRepository) {
        this.api = api;
        this.tokenStore = tokenStore;
        this.userRepository = userRepository;
    }

    public ApiResult<AppUserDto> guestLogin(String deviceName) {
        return guestLogin(deviceName, null);
    }

    public ApiResult<AppUserDto> guestLogin(String deviceName, String guestName) {
        try {
            Response<AuthResponse> response = api.guestLogin(new GuestLoginRequest(deviceName, guestName)).execute();
            if (response.isSuccessful() && response.body() != null) {
                tokenStore.save(response.body().token);
                userRepository.saveUser(response.body().user);
                return ApiResult.success(response.body().user);
            }
            return ApiResult.failure(parseError(response));
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<AppUserDto> login(String email, String password, String deviceName) {
        try {
            String guestToken = tokenStore.getToken();
            Response<AuthResponse> response = api.login(new LoginRequest(email, password, deviceName, guestToken)).execute();
            if (response.isSuccessful() && response.body() != null) {
                tokenStore.save(response.body().token);
                userRepository.saveUser(response.body().user);
                return ApiResult.success(response.body().user);
            }
            return ApiResult.failure(parseError(response));
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<AppUserDto> register(String name, String email, String password, String passwordConfirmation, String deviceName) {
        try {
            String guestToken = tokenStore.getToken();
            Response<AuthResponse> response = api.register(
                new RegisterRequest(name, email, password, passwordConfirmation, deviceName, guestToken)
            ).execute();
            if (response.isSuccessful() && response.body() != null) {
                tokenStore.save(response.body().token);
                userRepository.saveUser(response.body().user);
                return ApiResult.success(response.body().user);
            }
            return ApiResult.failure(parseError(response));
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<AppUserDto> loginWithGoogle(String idToken, String deviceName) {
        try {
            Response<AuthResponse> response = api.loginWithGoogle(new GoogleLoginRequest(idToken, deviceName)).execute();
            if (response.isSuccessful() && response.body() != null) {
                tokenStore.save(response.body().token);
                userRepository.saveUser(response.body().user);
                return ApiResult.success(response.body().user);
            }
            return ApiResult.failure(parseError(response));
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<String> sendOtp(String email) {
        try {
            Response<MessageResponse> response = api.sendOtp(new OtpSendRequest(email)).execute();
            if (response.isSuccessful() && response.body() != null) {
                return ApiResult.success(response.body().message);
            }
            return ApiResult.failure(parseError(response));
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<String> verifyOtp(String email, String code) {
        try {
            Response<MessageResponse> response = api.verifyOtp(new OtpVerifyRequest(email, code)).execute();
            if (response.isSuccessful() && response.body() != null) {
                return ApiResult.success(response.body().message);
            }
            return ApiResult.failure(parseError(response));
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<String> forgotPassword(String email) {
        try {
            Response<com.scholarlyapps.pathlingo.data.remote.dto.MessageResponse> response =
                api.forgotPassword(new ForgotPasswordRequest(email)).execute();
            if (response.isSuccessful() && response.body() != null) {
                return ApiResult.success(response.body().message);
            }
            return ApiResult.failure(parseError(response));
        } catch (IOException e) {
            return ApiResult.failure(e.getMessage());
        }
    }

    public ApiResult<Void> logout() {
        try {
            api.logout().execute();
        } catch (IOException ignored) {
        }
        tokenStore.clear();
        return ApiResult.success(null);
    }

    public String storedToken() {
        return tokenStore.getToken();
    }

    private String parseError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                return response.errorBody().string();
            }
        } catch (IOException ignored) {
        }
        return "Request failed: " + response.code();
    }
}
