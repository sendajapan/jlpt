package com.scholarlyapps.pathlingo.data.remote

import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto
import com.scholarlyapps.pathlingo.data.remote.dto.AuthResponse
import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse
import com.scholarlyapps.pathlingo.data.remote.dto.ForgotPasswordRequest
import com.scholarlyapps.pathlingo.data.remote.dto.GoogleLoginRequest
import com.scholarlyapps.pathlingo.data.remote.dto.ListResponse
import com.scholarlyapps.pathlingo.data.remote.dto.LoginRequest
import com.scholarlyapps.pathlingo.data.remote.dto.MessageResponse
import com.scholarlyapps.pathlingo.data.remote.dto.RegisterRequest
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("categories")
    suspend fun getCategories(): ListResponse<CategoryDto>

    @GET("subcategories")
    suspend fun getSubcategories(
        @Query("category_id") categoryId: Long? = null,
    ): ListResponse<SubcategoryDto>

    @GET("vocabularies")
    suspend fun getVocabularies(
        @Query("search") search: String? = null,
        @Query("category_id") categoryId: Long? = null,
        @Query("subcategory_id") subcategoryId: Long? = null,
    ): ListResponse<VocabularyDto>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

    @POST("auth/social/google")
    suspend fun loginWithGoogle(@Body body: GoogleLoginRequest): AuthResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): MessageResponse

    @POST("auth/logout")
    suspend fun logout(): MessageResponse

    @GET("me")
    suspend fun me(): WrappedResponse<AppUserDto>

    @GET("favorites")
    suspend fun favorites(): Map<String, Any?>

    @POST("favorites/{vocabulary}")
    suspend fun addFavorite(@Path("vocabulary") vocabularyId: Long): MessageResponse

    @DELETE("favorites/{vocabulary}")
    suspend fun removeFavorite(@Path("vocabulary") vocabularyId: Long): MessageResponse

    @GET("me/reads")
    suspend fun myReads(): Map<String, Any?>

    @POST("words/{vocabulary}/read")
    suspend fun markRead(@Path("vocabulary") vocabularyId: Long): Map<String, Any?>

    @GET("me/coins")
    suspend fun coinsBalance(): CoinsBalanceResponse

    @GET("me/coins/transactions")
    suspend fun coinsTransactions(): Map<String, Any?>
}
