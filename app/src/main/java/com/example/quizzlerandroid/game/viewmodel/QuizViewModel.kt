package com.example.quizzlerandroid.game.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.common.repository.OpenTriviaRepository
import com.example.quizzlerandroid.common.repository.QuizRepository
import com.example.quizzlerandroid.game.data.Quiz
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {
    private val errorLiveData = MutableLiveData<Boolean>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val questionsLiveData = MutableLiveData<List<Quiz>>()

    fun initGame() {
        loadingLiveData.value = true
        errorLiveData.value = false
        viewModelScope.launch {
            try {
                Log.i("initGame: ","fetching data")
                loadingLiveData.value = false
                val response = repository.getQuestions()
                questionsLiveData.value = response
                Log.i("initGame: ","fetched data")
            } catch (ex: Exception) {
                loadingLiveData.value = false
                errorLiveData.value = true
                Log.i("initGame: ",ex.toString())
            }

        }
    }

    fun loading(): LiveData<Boolean> = loadingLiveData
    fun error(): LiveData<Boolean> = errorLiveData
    fun questions(): LiveData<List<Quiz>> = questionsLiveData

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {

                return QuizViewModel(OpenTriviaRepository()) as T
            }
        }
    }
}