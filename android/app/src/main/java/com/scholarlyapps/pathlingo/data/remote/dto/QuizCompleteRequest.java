package com.scholarlyapps.pathlingo.data.remote.dto;

public class QuizCompleteRequest {
    public int score;
    public int total;

    public QuizCompleteRequest(int score, int total) {
        this.score = score;
        this.total = total;
    }
}
