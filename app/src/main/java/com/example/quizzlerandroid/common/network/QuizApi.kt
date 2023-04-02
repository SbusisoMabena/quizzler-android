package com.example.quizzlerandroid.common.network

import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.game.data.Quiz
import retrofit2.Call
import retrofit2.http.GET

interface QuizApi {
    @GET("api.php?amount=13&type=boolean&category=9")
    suspend fun getQuestions(): Data

    // TODO move instantiation logic from Repo to here
}