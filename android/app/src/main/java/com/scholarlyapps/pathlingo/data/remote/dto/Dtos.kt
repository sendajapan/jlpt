package com.scholarlyapps.pathlingo.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val id: Long,
    @Json(name = "name_en") val nameEn: String?,
    @Json(name = "name_jp") val nameJp: String?,
    @Json(name = "name_romaji") val nameRomaji: String?,
    @Json(name = "icon_url") val iconUrl: String?,
    @Json(name = "icon_thumbnail_url") val iconThumbnailUrl: String?,
    @Json(name = "category_bg_path") val categoryBgPath: String?,
    @Json(name = "bg_url") val bgUrl: String?,
    @Json(name = "audio_url") val audioUrl: String?,
    @Json(name = "sort_order") val sortOrder: Int?,
    @Json(name = "is_premium") val isPremium: Boolean = false,
)

@JsonClass(generateAdapter = true)
data class SubcategoryDto(
    val id: Long,
    @Json(name = "vocab_category_id") val vocabCategoryId: Long,
    @Json(name = "name_en") val nameEn: String?,
    @Json(name = "name_jp") val nameJp: String?,
    @Json(name = "name_romaji") val nameRomaji: String?,
    @Json(name = "icon_url") val iconUrl: String?,
    @Json(name = "icon_thumbnail_url") val iconThumbnailUrl: String?,
    @Json(name = "icon_thumbnail_bg") val iconThumbnailBg: String?,
    @Json(name = "audio_url") val audioUrl: String?,
    @Json(name = "sort_order") val sortOrder: Int?,
    @Json(name = "is_premium") val isPremium: Boolean = false,
)

@JsonClass(generateAdapter = true)
data class VocabularyDto(
    val id: Long,
    @Json(name = "vocab_subcategory_id") val vocabSubcategoryId: Long,
    @Json(name = "word_jp") val wordJp: String?,
    @Json(name = "word_romaji") val wordRomaji: String?,
    @Json(name = "word_en") val wordEn: String?,
    @Json(name = "sentence_jp") val sentenceJp: String?,
    @Json(name = "sentence_romaji") val sentenceRomaji: String?,
    @Json(name = "sentence_en") val sentenceEn: String?,
    @Json(name = "audio_jp_url") val audioJpUrl: String?,
    @Json(name = "audio_en_url") val audioEnUrl: String?,
    @Json(name = "sentence_audio_jp_url") val sentenceAudioJpUrl: String?,
    @Json(name = "sentence_audio_en_url") val sentenceAudioEnUrl: String?,
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "image_thumbnail_url") val imageThumbnailUrl: String?,
    @Json(name = "image_thumbnail_bg") val imageThumbnailBg: String?,
    @Json(name = "sort_order") val sortOrder: Int?,
    @Json(name = "is_premium") val isPremium: Boolean = false,
)

@JsonClass(generateAdapter = true)
data class ListResponse<T>(val data: List<T>)

@JsonClass(generateAdapter = true)
data class WrappedResponse<T>(val data: T)

@JsonClass(generateAdapter = true)
data class AppUserDto(
    val id: Long,
    val name: String?,
    val username: String?,
    val email: String?,
    @Json(name = "email_verified") val emailVerified: Boolean = false,
    val avatar: String?,
    @Json(name = "login_provider") val loginProvider: String?,
    @Json(name = "created_at") val createdAt: String?,
    val bio: String?,
    val gender: String?,
    @Json(name = "birth_date") val birthDate: String?,
    @Json(name = "native_language_code") val nativeLanguageCode: String?,
    @Json(name = "learning_language_code") val learningLanguageCode: String?,
    @Json(name = "proficiency_level") val proficiencyLevel: String?,
    @Json(name = "learning_goal") val learningGoal: String?,
    @Json(name = "daily_goal_minutes") val dailyGoalMinutes: Int = 0,
    val timezone: String?,
    @Json(name = "reminder_time") val reminderTime: String?,
    @Json(name = "notifications_enabled") val notificationsEnabled: Boolean = false,
    val coins: Int = 0,
    @Json(name = "xp_points") val xpPoints: Int = 0,
    @Json(name = "current_level") val currentLevel: Int = 0,
    @Json(name = "current_streak") val currentStreak: Int = 0,
    @Json(name = "longest_streak") val longestStreak: Int = 0,
    @Json(name = "learned_words") val learnedWords: Int = 0,
    @Json(name = "completed_lessons") val completedLessons: Int = 0,
    @Json(name = "total_study_minutes") val totalStudyMinutes: Int = 0,
    @Json(name = "last_streak_at") val lastStreakAt: String?,
    @Json(name = "last_seen_at") val lastSeenAt: String?,
    @Json(name = "last_lesson_at") val lastLessonAt: String?,
    @Json(name = "favorites_count") val favoritesCount: Int = 0,
    @Json(name = "reads_count") val readsCount: Int = 0,
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val user: AppUserDto,
    val token: String,
)

@JsonClass(generateAdapter = true)
data class MessageResponse(val message: String?)

@JsonClass(generateAdapter = true)
data class CoinsBalanceResponse(val coins: Int = 0)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @Json(name = "password_confirmation") val passwordConfirmation: String,
    @Json(name = "device_name") val deviceName: String,
    @Json(name = "display_name") val displayName: String? = null,
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String,
    @Json(name = "device_name") val deviceName: String,
)

@JsonClass(generateAdapter = true)
data class GoogleLoginRequest(
    @Json(name = "id_token") val idToken: String,
    @Json(name = "device_name") val deviceName: String,
)

@JsonClass(generateAdapter = true)
data class ForgotPasswordRequest(val email: String)
