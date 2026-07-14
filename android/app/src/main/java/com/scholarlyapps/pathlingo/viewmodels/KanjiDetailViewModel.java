package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.remote.dto.KanaLearnedResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.KanjiDto;
import com.scholarlyapps.pathlingo.data.repo.KanjiRepository;

public class KanjiDetailViewModel extends ViewModel {

    private final KanjiRepository repo;
    private final MutableLiveData<KanjiDto> kanji = new MutableLiveData<>();
    private final MutableLiveData<KanaLearnedResponse> learnResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saving = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public KanjiDetailViewModel(KanjiRepository repo) {
        this.repo = repo;
    }

    public LiveData<KanjiDto> getKanji() {
        return kanji;
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

    public void load(long kanjiId) {
        repo.getKanji(kanjiId, new KanjiRepository.Listener<>() {
            @Override
            public void onSuccess(KanjiDto data) {
                kanji.setValue(data);
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
            }
        });
    }

    public void markLearned(long kanjiId) {
        if (Boolean.TRUE.equals(saving.getValue())) return;
        saving.setValue(true);
        repo.markLearned(kanjiId, new KanjiRepository.Listener<>() {
            @Override
            public void onSuccess(KanaLearnedResponse data) {
                saving.setValue(false);
                KanjiDto current = kanji.getValue();
                if (current != null) {
                    current.isLearned = true;
                    kanji.setValue(current);
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
