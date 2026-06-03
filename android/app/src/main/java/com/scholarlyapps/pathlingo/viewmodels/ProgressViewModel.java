package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CatalogRepository;
import com.scholarlyapps.pathlingo.data.repo.UserRepository;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.User;

import java.util.List;

public class ProgressViewModel extends ViewModel {

    public final LiveData<User> user;
    public final LiveData<List<Category>> categories;

    public ProgressViewModel(CatalogRepository catalogRepo, UserRepository userRepo) {
        this.user = userRepo.getUser();
        this.categories = catalogRepo.getAllCategories();
    }
}
