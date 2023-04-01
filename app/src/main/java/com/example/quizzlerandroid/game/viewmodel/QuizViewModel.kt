package com.example.quizzlerandroid.game.viewmodel

import androidx.lifecycle.*
import com.example.quizzlerandroid.common.repository.OpenTriviaRepository
import com.example.quizzlerandroid.common.repository.QuizRepository
import com.example.quizzlerandroid.game.data.Quiz
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {
    private val errorLiveData = MutableLiveData<Boolean>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val game = MutableLiveData<GameState>()

    private lateinit var questions: List<Quiz>
    private var currentIndex = 0;

    data class GameState(
        val question: String = "",
        val totalQuestions: Int = 1,
        val questionCount: Int = 1,
        val score: Int = 0,
        val isGameOver: Boolean = false
    );

    fun initGame() {
        loadingLiveData.value = true
        errorLiveData.value = false
        viewModelScope.launch {
            try {
                loadingLiveData.value = false
                questions = repository.getQuestions()
                game.value = GameState(
                    question = questions.get(currentIndex).question,
                    totalQuestions = questions.size,
                    questionCount = currentIndex + 1
                )
                currentIndex++
            } catch (ex: Exception) {
                loadingLiveData.value = false
                errorLiveData.value = true
            }
        }
    }

    fun game(): LiveData<GameState> = game

    fun loading(): LiveData<Boolean> = loadingLiveData
    fun error(): LiveData<Boolean> = errorLiveData
    fun answer() {
        if (game.value!!.isGameOver) {
            return
        }
        game.value = game.value!!.copy(
            question = questions.get(currentIndex).question,
            questionCount = currentIndex + 1,
            isGameOver = game.value!!.questionCount + 1 == questions.size
        )
        currentIndex++
    }

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