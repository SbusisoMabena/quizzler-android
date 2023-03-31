package com.example.quizzlerandroid.api

import com.example.quizzlerandroid.Data
import retrofit2.Call
import retrofit2.http.GET

interface QuizApi {
    @GET("api.php?amount=10&difficulty=medium&type=boolean")
    suspend fun quiz(): Data

    companion object {
        const val BASE_URL = "https://opentdb.com/"
    }
}