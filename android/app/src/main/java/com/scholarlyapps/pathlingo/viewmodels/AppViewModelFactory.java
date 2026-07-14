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
            return (T) new HomeViewModel(ServiceLocator.categoryRepository, ServiceLocator.userRepository);
        }
        if (modelClass == CategoryListViewModel.class) {
            return (T) new CategoryListViewModel(ServiceLocator.categoryRepository);
        }
        if (modelClass == SubcategoryViewModel.class) {
            return (T) new SubcategoryViewModel(ServiceLocator.categoryRepository);
        }
        if (modelClass == WordDetailViewModel.class) {
            return (T) new WordDetailViewModel(ServiceLocator.categoryRepository);
        }
        if (modelClass == FavoritesViewModel.class) {
            return (T) new FavoritesViewModel(ServiceLocator.categoryRepository);
        }
        if (modelClass == ProgressViewModel.class) {
            return (T) new ProgressViewModel(ServiceLocator.categoryRepository, ServiceLocator.userRepository);
        }
        if (modelClass == EditProfileViewModel.class) {
            return (T) new EditProfileViewModel(ServiceLocator.userRepository);
        }
        if (modelClass == ChangePasswordViewModel.class) {
            return (T) new ChangePasswordViewModel(ServiceLocator.authRepository, ServiceLocator.userRepository);
        }
        if (modelClass == KanaChartViewModel.class) {
            return (T) new KanaChartViewModel(ServiceLocator.kanaRepository);
        }
        if (modelClass == KanaDetailViewModel.class) {
            return (T) new KanaDetailViewModel(ServiceLocator.kanaRepository);
        }
        if (modelClass == KanjiGridViewModel.class) {
            return (T) new KanjiGridViewModel(ServiceLocator.kanjiRepository);
        }
        if (modelClass == KanjiStrokeViewModel.class) {
            return (T) new KanjiStrokeViewModel(ServiceLocator.kanjiRepository);
        }
        if (modelClass == KanjiDetailViewModel.class) {
            return (T) new KanjiDetailViewModel(ServiceLocator.kanjiRepository);
        }
        if (modelClass == QuizViewModel.class) {
            return (T) new QuizViewModel(ServiceLocator.api, ServiceLocator.wordActionRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
    }
}
