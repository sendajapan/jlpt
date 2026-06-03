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

    private final ApiService api;
    private final UserDao userDao;

    public UserRepository(ApiService api, UserDao userDao) {
        this.api = api;
        this.userDao = userDao;
    }

    public LiveData<User> getUser() {
        return Transformations.map(userDao.getUser(), entity -> {
            if (entity == null) return null;
            User u = new User();
            u.name = orEmpty(entity.name);
            u.jpName = orEmpty(entity.username);
            u.level = entity.currentLevel;
            u.xp = entity.xpPoints;
            u.maxXp = 100;
            u.streak = entity.currentStreak;
            u.wordsKnown = entity.learnedWords;
            u.email = orEmpty(entity.email);
            return u;
        });
    }

    public boolean refresh() {
        try {
            Response<WrappedResponse<AppUserDto>> response = api.me().execute();
            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
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
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String orEmpty(String s) {
        return s != null ? s : "";
    }
}
