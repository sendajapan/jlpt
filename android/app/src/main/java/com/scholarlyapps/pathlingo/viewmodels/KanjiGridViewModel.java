package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.remote.dto.KanjiDto;
import com.scholarlyapps.pathlingo.data.remote.dto.PagedResponse;
import com.scholarlyapps.pathlingo.data.repo.KanjiRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KanjiGridViewModel extends ViewModel {

    private static final int PER_PAGE = 60;

    private final KanjiRepository repo;
    private final MutableLiveData<List<KanjiDto>> items = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loadingMore = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private String jlpt;
    private Integer strokes;
    private int currentPage = 0;
    private int lastPage = 1;
    private boolean loading = false;

    public KanjiGridViewModel(KanjiRepository repo) {
        this.repo = repo;
    }

    public LiveData<List<KanjiDto>> getItems() {
        return items;
    }

    public LiveData<Boolean> getLoadingMore() {
        return loadingMore;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void setJlpt(String level) {
        jlpt = level;
        strokes = null;
        reset();
        loadNextPage();
    }

    public void setStrokes(int strokeCount) {
        strokes = strokeCount;
        jlpt = null;
        reset();
        loadNextPage();
    }

    public boolean canLoadMore() {
        return !loading && currentPage < lastPage;
    }

    public void loadNextPage() {
        if (!canLoadMore()) return;
        loading = true;
        loadingMore.setValue(true);

        repo.getKanjis(jlpt, strokes, currentPage + 1, PER_PAGE, new KanjiRepository.Listener<>() {
            @Override
            public void onSuccess(PagedResponse<KanjiDto> data) {
                loading = false;
                loadingMore.setValue(false);
                if (data.meta != null) {
                    currentPage = data.meta.currentPage;
                    lastPage = data.meta.lastPage;
                } else {
                    currentPage++;
                    lastPage = currentPage;
                }
                List<KanjiDto> current = new ArrayList<>(items.getValue() != null ? items.getValue() : new ArrayList<>());
                current.addAll(data.getData());
                items.setValue(current);
            }

            @Override
            public void onError(String message) {
                loading = false;
                loadingMore.setValue(false);
                error.setValue(message);
            }
        });
    }

    public void refreshLearned() {
        List<KanjiDto> current = items.getValue();
        if (current == null || current.isEmpty()) return;

        repo.learnedIds(new KanjiRepository.Listener<>() {
            @Override
            public void onSuccess(List<Long> ids) {
                List<KanjiDto> loaded = items.getValue();
                if (loaded == null) return;
                Set<Long> learned = new HashSet<>(ids);
                boolean changed = false;
                for (KanjiDto kanji : loaded) {
                    boolean isLearned = learned.contains(kanji.id);
                    if (kanji.isLearned != isLearned) {
                        kanji.isLearned = isLearned;
                        changed = true;
                    }
                }
                if (changed) items.setValue(loaded);
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    public void clearError() {
        error.setValue(null);
    }

    private void reset() {
        currentPage = 0;
        lastPage = 1;
        loading = false;
        items.setValue(new ArrayList<>());
    }
}
