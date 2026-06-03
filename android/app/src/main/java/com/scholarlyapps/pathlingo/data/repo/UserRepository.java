package com.scholarlyapps.pathlingo.data.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.scholarlyapps.pathlingo.data.local.db.dao.UserDao;
import com.scholarlyapps.pathlingo.data.local.db.entity.UserEntity;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse;
import com.scholarlyapps.pathlingo.models.User;

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
            user.email = orEmpty(entity.email);
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
            entity.currentLevel = dto.currentLevel;
            entity.xpPoints = dto.xpPoints;
            entity.currentStreak = dto.currentStreak;
            entity.learnedWords = dto.learnedWords;
            userDao.insert(entity);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private String orEmpty(String value) {
        return value != null ? value : "";
    }
}
