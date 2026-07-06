package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.remote.dto.KanaDto;
import com.scholarlyapps.pathlingo.data.repo.KanaRepository;

import java.util.List;

public class KanaChartViewModel extends ViewModel {

    private final KanaRepository repo;
    private final MutableLiveData<List<KanaDto>> kanas = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public KanaChartViewModel(KanaRepository repo) {
        this.repo = repo;
    }

    public LiveData<List<KanaDto>> getKanas() {
        return kanas;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void load(String script) {
        repo.getKanas(script, new KanaRepository.Listener<>() {
            @Override
            public void onSuccess(List<KanaDto> data) {
                kanas.setValue(data);
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
            }
        });
    }

    public void clearError() {
        error.setValue(null);
    }
}
