package com.scholarlyapps.pathlingo.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;

public class AppViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == HomeViewModel.class) {
            return (T) new HomeViewModel(ServiceLocator.catalogRepository, ServiceLocator.userRepository);
        }
        if (modelClass == CategoryListViewModel.class) {
            return (T) new CategoryListViewModel(ServiceLocator.catalogRepository);
        }
        if (modelClass == SubcategoryViewModel.class) {
            return (T) new SubcategoryViewModel(ServiceLocator.catalogRepository);
        }
        if (modelClass == WordDetailViewModel.class) {
            return (T) new WordDetailViewModel(ServiceLocator.catalogRepository);
        }
        if (modelClass == FavoritesViewModel.class) {
            return (T) new FavoritesViewModel(ServiceLocator.catalogRepository);
        }
        if (modelClass == ProgressViewModel.class) {
            return (T) new ProgressViewModel(ServiceLocator.catalogRepository, ServiceLocator.userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
    }
}
