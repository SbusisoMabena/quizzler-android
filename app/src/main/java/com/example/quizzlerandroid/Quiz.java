package com.example.quizzlerandroid;

import com.google.gson.annotations.SerializedName;

public class Quiz {
    @SerializedName("question")
    private String question;
    @SerializedName("correct_answer")
    private boolean answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
