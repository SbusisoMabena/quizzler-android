package com.example.quizzlerandroid.common.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.common.network.QuizApi
import com.example.quizzlerandroid.game.data.Quiz
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenTriviaRepository() : QuizRepository {
    private val baseUrl = "https://opentdb.com/"
    private var quizApi: QuizApi

    init {
        val retrofit =
            Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
                .build()
        quizApi = retrofit.create(QuizApi::class.java)
    }

    override suspend fun getQuestions(): List<Quiz> {
        return quizApi.getQuestions().data!!
    }
}