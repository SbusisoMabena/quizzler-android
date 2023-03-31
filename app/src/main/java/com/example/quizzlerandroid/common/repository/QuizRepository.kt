package com.example.quizzlerandroid.common.repository

import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.game.data.Quiz
import retrofit2.Call

interface QuizRepository {
    suspend fun getQuestions(): List<Quiz>

}
