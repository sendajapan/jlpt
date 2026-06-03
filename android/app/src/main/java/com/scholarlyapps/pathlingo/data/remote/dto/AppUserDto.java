package com.scholarlyapps.pathlingo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class AppUserDto {
    public long id;
    public String name;
    public String username;
    public String email;
    @SerializedName("email_verified")
    public boolean emailVerified;
    public String avatar;
    @SerializedName("login_provider")
    public String loginProvider;
    @SerializedName("is_guest")
    public boolean isGuest;
    @SerializedName("created_at")
    public String createdAt;
    public String bio;
    public String gender;
    @SerializedName("birth_date")
    public String birthDate;
    @SerializedName("native_language_code")
    public String nativeLanguageCode;
    @SerializedName("learning_language_code")
    public String learningLanguageCode;
    @SerializedName("proficiency_level")
    public String proficiencyLevel;
    @SerializedName("learning_goal")
    public String learningGoal;
    @SerializedName("daily_goal_minutes")
    public int dailyGoalMinutes;
    public String timezone;
    @SerializedName("reminder_time")
    public String reminderTime;
    @SerializedName("notifications_enabled")
    public boolean notificationsEnabled;
    public int coins;
    @SerializedName("xp_points")
    public int xpPoints;
    @SerializedName("current_level")
    public int currentLevel;
    @SerializedName("current_streak")
    public int currentStreak;
    @SerializedName("longest_streak")
    public int longestStreak;
    @SerializedName("learned_words")
    public int learnedWords;
    @SerializedName("completed_lessons")
    public int completedLessons;
    @SerializedName("total_study_minutes")
    public int totalStudyMinutes;
    @SerializedName("last_streak_at")
    public String lastStreakAt;
    @SerializedName("last_seen_at")
    public String lastSeenAt;
    @SerializedName("last_lesson_at")
    public String lastLessonAt;
    @SerializedName("favorites_count")
    public int favoritesCount;
    @SerializedName("reads_count")
    public int readsCount;
}
