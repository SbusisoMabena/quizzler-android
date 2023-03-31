package com.example.quizzlerandroid.common.network

import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.game.data.Quiz
import retrofit2.Call
import retrofit2.http.GET

interface QuizApi {
    @GET("api.php?amount=10&difficulty=medium&type=boolean&category=18")
    suspend fun getQuestions(): Data

    // TODO move instantiation logic from Repo to here
}