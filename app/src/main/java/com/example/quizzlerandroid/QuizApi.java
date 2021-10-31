package com.example.quizzlerandroid;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizApi {

    String BASE_URL = "https://opentdb.com/";

    @GET("api.php?amount=10&difficulty=medium&type=boolean")
    Call<Data> getQuiz();

}
