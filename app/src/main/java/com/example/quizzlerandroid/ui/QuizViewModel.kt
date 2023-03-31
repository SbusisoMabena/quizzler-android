package com.example.quizzlerandroid.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.api.QuizApi
import com.example.quizzlerandroid.api.RetrofitInstance
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    val questions = MutableLiveData<Data>()
    fun getQuestions() {
        viewModelScope.launch {
            val result = RetrofitInstance.api.quiz()
            questions.value = result
        }
    }
}