package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.remote.dto.KanaDto;
import com.scholarlyapps.pathlingo.data.remote.dto.KanaLearnedResponse;
import com.scholarlyapps.pathlingo.data.repo.KanaRepository;

public class KanaDetailViewModel extends ViewModel {

    private final KanaRepository repo;
    private final MutableLiveData<KanaDto> kana = new MutableLiveData<>();
    private final MutableLiveData<KanaLearnedResponse> learnResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saving = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public KanaDetailViewModel(KanaRepository repo) {
        this.repo = repo;
    }

    public LiveData<KanaDto> getKana() {
        return kana;
    }

    public LiveData<KanaLearnedResponse> getLearnResult() {
        return learnResult;
    }

    public LiveData<Boolean> getSaving() {
        return saving;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void load(long kanaId) {
        repo.getKana(kanaId, new KanaRepository.Listener<>() {
            @Override
            public void onSuccess(KanaDto data) {
                kana.setValue(data);
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
            }
        });
    }

    public void markLearned(long kanaId) {
        if (Boolean.TRUE.equals(saving.getValue())) return;
        saving.setValue(true);
        repo.markLearned(kanaId, new KanaRepository.Listener<>() {
            @Override
            public void onSuccess(KanaLearnedResponse data) {
                saving.setValue(false);
                KanaDto current = kana.getValue();
                if (current != null) {
                    current.isLearned = true;
                    kana.setValue(current);
                }
                learnResult.setValue(data);
            }

            @Override
            public void onError(String message) {
                saving.setValue(false);
                error.setValue(message);
            }
        });
    }

    public void clearError() {
        error.setValue(null);
    }
}
