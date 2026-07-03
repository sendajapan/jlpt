package com.scholarlyapps.pathlingo.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.networking.ApiErrors;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizCompleteRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizCompleteResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizQuestionDto;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizResponse;
import com.scholarlyapps.pathlingo.data.repo.WordActionRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizViewModel extends ViewModel {

    public enum QuestionType {TEXT, IMAGE, AUDIO}

    public static class QuizQuestion {
        public QuestionType type;
        public String wordJp;
        public String wordRomaji;
        public String wordEn;
        public String imageUrl;
        public String audioJpUrl;
        public String audioEnUrl;
        public String correctAnswer;
        public List<String> options;
    }

    public static class QuizResult {
        public int coinsEarned;
        public int xpEarned;
        public boolean pendingSync;
    }

    private final ApiService api;
    private final WordActionRepository wordActionRepository;
    private List<QuizQuestion> questions = new ArrayList<>();
    private List<Boolean> answerResults = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private long quizStartTime = 0;
    private long quizEndTime = 0;

    private final MutableLiveData<QuizQuestion> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Boolean> quizLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> submitting = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<QuizResult> quizResult = new MutableLiveData<>();

    public QuizViewModel(ApiService api, WordActionRepository wordActionRepository) {
        this.api = api;
        this.wordActionRepository = wordActionRepository;
    }

    public LiveData<QuizQuestion> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Boolean> getQuizLoaded() {
        return quizLoaded;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getSubmitting() {
        return submitting;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<QuizResult> getQuizResult() {
        return quizResult;
    }

    public void clearQuizResult() {
        quizResult.setValue(null);
    }

    public void clearError() {
        error.setValue(null);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return questions.size();
    }

    public boolean isFinished() {
        return currentIndex >= questions.size();
    }

    public void loadQuiz() {
        currentIndex = 0;
        score = 0;
        quizStartTime = 0;
        quizEndTime = 0;
        questions.clear();
        answerResults.clear();
        loading.setValue(true);
        api.generateQuiz().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<QuizResponse> call, @NonNull Response<QuizResponse> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    questions = mapQuestions(response.body().data);
                    if (questions.isEmpty()) {
                        error.postValue("Not enough words available for a quiz yet.");
                    } else {
                        quizLoaded.postValue(true);
                    }
                } else {
                    error.postValue(ApiErrors.message(response, "Failed to load quiz."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizResponse> call, @NonNull Throwable t) {
                loading.postValue(false);
                error.postValue(ApiErrors.message(t));
            }
        });
    }

    public void advance() {
        if (currentIndex >= questions.size()) return;
        if (currentIndex == 0) quizStartTime = System.currentTimeMillis();
        currentQuestion.setValue(questions.get(currentIndex));
    }

    public void recordAnswer(boolean isCorrect) {
        if (isCorrect) score++;
        answerResults.add(isCorrect);
        currentIndex++;
    }

    public List<Boolean> getAnswerResults() {
        return new ArrayList<>(answerResults);
    }

    public long getTimeTakenMillis() {
        return quizEndTime > quizStartTime ? quizEndTime - quizStartTime : 0;
    }

    public void submitResult() {
        quizEndTime = System.currentTimeMillis();
        submitting.setValue(true);
        api.completeQuiz(new QuizCompleteRequest(score, questions.size())).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<QuizCompleteResponse> call, @NonNull Response<QuizCompleteResponse> response) {
                submitting.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    QuizResult result = new QuizResult();
                    result.coinsEarned = response.body().coinsEarned;
                    result.xpEarned = response.body().xpEarned;
                    quizResult.postValue(result);
                } else {
                    quizResult.postValue(queueAndBuildLocalResult());
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizCompleteResponse> call, @NonNull Throwable t) {
                submitting.postValue(false);
                quizResult.postValue(queueAndBuildLocalResult());
            }
        });
    }

    private QuizResult queueAndBuildLocalResult() {
        wordActionRepository.queueQuizCompletion(score, questions.size());
        QuizResult result = new QuizResult();
        result.coinsEarned = score * 10;
        result.xpEarned = score * 5;
        result.pendingSync = true;
        return result;
    }

    private List<QuizQuestion> mapQuestions(List<QuizQuestionDto> dtos) {
        List<QuizQuestion> list = new ArrayList<>();
        if (dtos == null) return list;
        for (QuizQuestionDto dto : dtos) {
            QuizQuestion q = new QuizQuestion();
            q.type = parseType(dto.type);
            q.wordJp = orEmpty(dto.word.wordJp);
            q.wordRomaji = orEmpty(dto.word.wordRomaji);
            q.wordEn = orEmpty(dto.word.wordEn);
            q.imageUrl = dto.word.imageUrl;
            q.audioJpUrl = dto.word.audioJpUrl;
            q.audioEnUrl = dto.word.audioEnUrl;
            q.correctAnswer = dto.correctAnswer;
            q.options = dto.options;
            list.add(q);
        }
        return list;
    }

    private QuestionType parseType(String type) {
        if ("image".equals(type)) return QuestionType.IMAGE;
        if ("audio".equals(type)) return QuestionType.AUDIO;
        return QuestionType.TEXT;
    }

    private String orEmpty(String s) {
        return s != null ? s : "";
    }
}
