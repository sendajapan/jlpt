package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizCompleteRequest;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizCompleteResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizQuestionDto;
import com.scholarlyapps.pathlingo.data.remote.dto.QuizResponse;

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

    private final ApiService api;
    private List<QuizQuestion> questions = new ArrayList<>();
    private List<Boolean> answerResults = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private long quizStartTime = 0;
    private long quizEndTime = 0;

    private final MutableLiveData<QuizQuestion> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<Boolean> quizLoaded = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<QuizCompleteResponse> quizResult = new MutableLiveData<>();

    public QuizViewModel(ApiService api) {
        this.api = api;
    }

    public LiveData<QuizQuestion> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<Boolean> getQuizLoaded() {
        return quizLoaded;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<QuizCompleteResponse> getQuizResult() {
        return quizResult;
    }

    public void clearQuizResult() {
        quizResult.setValue(null);
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
        api.generateQuiz().enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = mapQuestions(response.body().data);
                    quizLoaded.postValue(true);
                } else {
                    error.postValue("Failed to load quiz");
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                error.postValue("Network error: " + t.getMessage());
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
        api.completeQuiz(new QuizCompleteRequest(score, questions.size())).enqueue(new Callback<QuizCompleteResponse>() {
            @Override
            public void onResponse(Call<QuizCompleteResponse> call, Response<QuizCompleteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quizResult.postValue(response.body());
                } else {
                    quizResult.postValue(fallbackResult());
                }
            }

            @Override
            public void onFailure(Call<QuizCompleteResponse> call, Throwable t) {
                quizResult.postValue(fallbackResult());
            }
        });
    }

    private QuizCompleteResponse fallbackResult() {
        QuizCompleteResponse r = new QuizCompleteResponse();
        r.coinsEarned = score * 10;
        r.xpEarned = score * 5;
        return r;
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
