package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.repo.CategoryRepository;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class QuizViewModel extends ViewModel {

    public enum QuestionType { TEXT, IMAGE, AUDIO }

    public static class QuizQuestion {
        public QuestionType type;
        public Word correct;
        public List<String> options;
    }

    private final CategoryRepository repo;
    private List<Word> words = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    private final MutableLiveData<QuizQuestion> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Boolean> wordsLoaded = new MutableLiveData<>();

    public QuizViewModel(CategoryRepository repo) {
        this.repo = repo;
    }

    public LiveData<QuizQuestion> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Boolean> getWordsLoaded() {
        return wordsLoaded;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return words.size();
    }

    public boolean isFinished() {
        return currentIndex >= words.size();
    }

    public void loadWords() {
        currentIndex = 0;
        score = 0;
        words.clear();
        Executors.newSingleThreadExecutor().execute(() -> {
            words = new ArrayList<>(repo.getRandomWords(10));
            wordsLoaded.postValue(true);
        });
    }

    public void advance() {
        if (currentIndex >= words.size()) return;
        Word correct = words.get(currentIndex);
        QuizQuestion question = new QuizQuestion();
        question.correct = correct;
        question.type = pickType(correct);
        question.options = buildOptions(correct, question.type);
        currentQuestion.setValue(question);
    }

    public void recordAnswer(boolean isCorrect) {
        if (isCorrect) score++;
        currentIndex++;
    }

    private QuestionType pickType(Word word) {
        List<QuestionType> valid = new ArrayList<>();
        valid.add(QuestionType.TEXT);
        if (word.img != null && !word.img.isEmpty()) valid.add(QuestionType.IMAGE);
        if (word.audioUrl != null && !word.audioUrl.isEmpty()) valid.add(QuestionType.AUDIO);
        Collections.shuffle(valid);
        return valid.get(0);
    }

    private List<String> buildOptions(Word correct, QuestionType type) {
        List<Word> distractors = new ArrayList<>();
        for (Word w : words) {
            if (w.id != correct.id) distractors.add(w);
        }
        Collections.shuffle(distractors);

        List<String> options = new ArrayList<>();
        options.add(displayText(correct, type));
        for (int i = 0; i < 3 && i < distractors.size(); i++) {
            options.add(displayText(distractors.get(i), type));
        }
        Collections.shuffle(options);
        return options;
    }

    private String displayText(Word word, QuestionType type) {
        if (type == QuestionType.TEXT) {
            return word.en;
        } else {
            return word.kanji.isEmpty() ? word.romaji : word.kanji;
        }
    }
}
