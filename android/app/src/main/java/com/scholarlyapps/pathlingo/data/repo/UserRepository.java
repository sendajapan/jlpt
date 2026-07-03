package com.scholarlyapps.pathlingo.data.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.scholarlyapps.pathlingo.data.ApiResult;
import com.scholarlyapps.pathlingo.data.local.db.dao.UserDao;
import com.scholarlyapps.pathlingo.data.local.db.entity.UserEntity;
import com.scholarlyapps.pathlingo.data.networking.ApiErrors;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse;
import com.scholarlyapps.pathlingo.models.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UserRepository {

    private final ApiService apiService;
    private final UserDao userDao;

    public UserRepository(ApiService apiService, UserDao userDao) {
        this.apiService = apiService;
        this.userDao = userDao;
    }

    public LiveData<User> getUser() {
        return Transformations.map(userDao.getUser(), entity -> {
            if (entity == null) return null;
            User user = new User();
            user.name = orEmpty(entity.name);
            user.jpName = orEmpty(entity.username);
            user.level = entity.currentLevel;
            user.xp = entity.xpPoints;
            user.maxXp = 100;
            user.streak = entity.currentStreak;
            user.wordsKnown = entity.learnedWords;
            user.coins = entity.coins;
            user.email = orEmpty(entity.email);
            user.avatarUrl = entity.avatarUrl;
            return user;
        });
    }

    public boolean refresh() {
        try {
            Response<WrappedResponse<AppUserDto>> response = apiService.me().execute();
            if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                return false;
            }
            AppUserDto dto = response.body().getData();
            UserEntity entity = new UserEntity();
            entity.id = dto.id;
            entity.name = dto.name;
            entity.username = dto.username;
            entity.email = dto.email;
            entity.avatarUrl = dto.avatarUrl != null ? dto.avatarUrl : dto.avatar;
            entity.currentLevel = dto.currentLevel;
            entity.xpPoints = dto.xpPoints;
            entity.currentStreak = dto.currentStreak;
            entity.learnedWords = dto.learnedWords;
            entity.coins = dto.coins;
            userDao.insert(entity);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public AppUserDto fetchAndSave() {
        try {
            Response<WrappedResponse<AppUserDto>> response = apiService.me().execute();
            if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                return null;
            }
            AppUserDto dto = response.body().getData();
            UserEntity entity = new UserEntity();
            entity.id = dto.id;
            entity.name = dto.name;
            entity.username = dto.username;
            entity.email = dto.email;
            entity.avatarUrl = dto.avatarUrl != null ? dto.avatarUrl : dto.avatar;
            entity.currentLevel = dto.currentLevel;
            entity.xpPoints = dto.xpPoints;
            entity.currentStreak = dto.currentStreak;
            entity.learnedWords = dto.learnedWords;
            entity.coins = dto.coins;
            userDao.insert(entity);
            return dto;
        } catch (Exception ignored) {
            return null;
        }
    }

    public void saveUser(AppUserDto dto) {
        UserEntity entity = new UserEntity();
        entity.id = dto.id;
        entity.name = dto.name;
        entity.username = dto.username;
        entity.email = dto.email;
        entity.avatarUrl = dto.avatarUrl != null ? dto.avatarUrl : dto.avatar;
        entity.currentLevel = dto.currentLevel;
        entity.xpPoints = dto.xpPoints;
        entity.currentStreak = dto.currentStreak;
        entity.learnedWords = dto.learnedWords;
        entity.coins = dto.coins;
        userDao.deleteAll();
        userDao.insert(entity);
    }

    public ApiResult<AppUserDto> fetchProfile() {
        try {
            Response<WrappedResponse<AppUserDto>> response = apiService.me().execute();
            if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                return ApiResult.failure(ApiErrors.message(response, "Failed to load profile."));
            }
            return ApiResult.success(response.body().getData());
        } catch (Exception e) {
            return ApiResult.failure(ApiErrors.message(e));
        }
    }

    public ApiResult<AppUserDto> updateProfile(String name, String username, String bio, String gender, String birthDate,
                                               String proficiencyLevel, String learningGoal, String dailyGoalMinutes) {
        try {
            Map<String, RequestBody> fields = new HashMap<>();
            fields.put("name", toBody(name));
            if (username != null && !username.isEmpty()) {
                fields.put("username", toBody(username));
            }
            if (bio != null && !bio.isEmpty()) {
                fields.put("bio", toBody(bio));
            }
            if (gender != null && !gender.isEmpty()) {
                fields.put("gender", toBody(gender));
            }
            if (birthDate != null && !birthDate.isEmpty()) {
                fields.put("birth_date", toBody(birthDate));
            }
            if (proficiencyLevel != null && !proficiencyLevel.isEmpty()) {
                fields.put("proficiency_level", toBody(proficiencyLevel));
            }
            if (learningGoal != null && !learningGoal.isEmpty()) {
                fields.put("learning_goal", toBody(learningGoal));
            }
            if (dailyGoalMinutes != null && !dailyGoalMinutes.isEmpty()) {
                fields.put("daily_goal_minutes", toBody(dailyGoalMinutes));
            }
            Response<WrappedResponse<AppUserDto>> response = apiService.updateProfile(fields).execute();
            if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                return ApiResult.failure(ApiErrors.message(response, "Failed to update profile."));
            }
            AppUserDto dto = response.body().getData();
            UserEntity entity = new UserEntity();
            entity.id = dto.id;
            entity.name = dto.name;
            entity.username = dto.username;
            entity.email = dto.email;
            entity.avatarUrl = dto.avatarUrl != null ? dto.avatarUrl : dto.avatar;
            entity.currentLevel = dto.currentLevel;
            entity.xpPoints = dto.xpPoints;
            entity.currentStreak = dto.currentStreak;
            entity.learnedWords = dto.learnedWords;
            entity.coins = dto.coins;
            userDao.insert(entity);
            return ApiResult.success(dto);
        } catch (Exception e) {
            return ApiResult.failure(ApiErrors.message(e));
        }
    }

    private RequestBody toBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private String orEmpty(String value) {
        return value != null ? value : "";
    }
}
