package com.example.quizzlerandroid.game.model

class Game {
    var currentScore: Int = 0
        private set

    fun incrementScore() {
        currentScore++
    }

}
