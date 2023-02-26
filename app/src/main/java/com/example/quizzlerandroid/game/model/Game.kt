package com.example.quizzlerandroid.game.model

class Game(private val questions: List<Question>) {
    var currentScore: Int = 0
        private set

    private var currentQuestionIndex = -1

    fun incrementScore() {
        currentScore++
    }

    fun nextQuestion(): Question? {
        return if (currentQuestionIndex + 1 < questions.size) {
            currentQuestionIndex++
            questions[currentQuestionIndex]
        } else {
            null
        }
    }

}
