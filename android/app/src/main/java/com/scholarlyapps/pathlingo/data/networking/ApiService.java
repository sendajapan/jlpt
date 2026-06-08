package com.scholarlyapps.pathlingo.data.networking;

import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.remote.dto.AvatarDto;
import com.scholarlyapps.pathlingo.data.remote.dto.AuthResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.ForgotPasswordRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.GoogleLoginRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.GuestLoginRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.ListResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.LoginRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.MessageResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.OtpSendRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.OtpVerifyRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.RegisterRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto;
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("categories")
    Call<ListResponse<CategoryDto>> getCategories();

    @GET("subcategories")
    Call<ListResponse<SubcategoryDto>> getSubcategories(@Query("category_id") Long categoryId);

    @GET("vocabularies")
    Call<ListResponse<VocabularyDto>> getVocabularies(
        @Query("search") String search,
        @Query("category_id") Long categoryId,
        @Query("subcategory_id") Long subcategoryId
    );

    @POST("auth/guest")
    Call<AuthResponse> guestLogin(@Body GuestLoginRequest body);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest body);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest body);

    @POST("auth/social/google")
    Call<AuthResponse> loginWithGoogle(@Body GoogleLoginRequest body);

    @POST("auth/otp/send")
    Call<MessageResponse> sendOtp(@Body OtpSendRequest body);

    @POST("auth/otp/verify")
    Call<MessageResponse> verifyOtp(@Body OtpVerifyRequest body);

    @POST("auth/forgot-password")
    Call<MessageResponse> forgotPassword(@Body ForgotPasswordRequest body);

    @POST("auth/logout")
    Call<MessageResponse> logout();

    @GET("me")
    Call<WrappedResponse<AppUserDto>> me();

    @Multipart
    @POST("me")
    Call<WrappedResponse<AppUserDto>> updateProfile(@PartMap Map<String, RequestBody> fields);

    @GET("favorites")
    Call<Map<String, Object>> favorites();

    @POST("favorites/{vocabulary}")
    Call<MessageResponse> addFavorite(@Path("vocabulary") long vocabularyId);

    @DELETE("favorites/{vocabulary}")
    Call<MessageResponse> removeFavorite(@Path("vocabulary") long vocabularyId);

    @GET("me/reads")
    Call<Map<String, Object>> myReads();

    @POST("words/{vocabulary}/read")
    Call<Map<String, Object>> markRead(@Path("vocabulary") long vocabularyId);

    @GET("me/coins")
    Call<CoinsBalanceResponse> coinsBalance();

    @GET("me/coins/transactions")
    Call<Map<String, Object>> coinsTransactions();

    @GET("avatars")
    Call<ListResponse<AvatarDto>> getAvatars();

    @POST("avatars/{id}/purchase")
    Call<WrappedResponse<CoinsBalanceResponse>> purchaseAvatar(@Path("id") long avatarId);

    @PATCH("me/avatar")
    Call<WrappedResponse<AppUserDto>> setActiveAvatar(@Body Map<String, Long> body);

    @POST("words/{id}/unlock")
    Call<MessageResponse> unlockWord(@Path("id") long wordId);

    @POST("categories/{id}/unlock")
    Call<MessageResponse> unlockCategory(@Path("id") long categoryId);

    @POST("subcategories/{id}/unlock")
    Call<MessageResponse> unlockSubcategory(@Path("id") long subcategoryId);
}
