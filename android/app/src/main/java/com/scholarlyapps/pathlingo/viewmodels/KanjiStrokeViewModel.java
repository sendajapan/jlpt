package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.remote.dto.KanjiStrokeGroupDto;
import com.scholarlyapps.pathlingo.data.repo.KanjiRepository;

import java.util.List;

public class KanjiStrokeViewModel extends ViewModel {

    private final KanjiRepository repo;
    private final MutableLiveData<List<KanjiStrokeGroupDto>> groups = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public KanjiStrokeViewModel(KanjiRepository repo) {
        this.repo = repo;
    }

    public LiveData<List<KanjiStrokeGroupDto>> getGroups() {
        return groups;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void load() {
        repo.getStrokeGroups(new KanjiRepository.Listener<>() {
            @Override
            public void onSuccess(List<KanjiStrokeGroupDto> data) {
                groups.setValue(data);
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
