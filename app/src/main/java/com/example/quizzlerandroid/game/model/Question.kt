package com.example.quizzlerandroid.game.model

class Question(question: String, private val answer: Boolean) {

    var answeredOption: Boolean? = null
        private set

    fun answer(option: Boolean): Boolean {
        answeredOption = option
        return option == answer
    }
}
