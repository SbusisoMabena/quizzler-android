package com.example.quizzlerandroid.game.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GameUnitTests {
    private lateinit var game: Game

    @Before
    fun setup() {
        game = Game(listOf())
    }

    @Test
    fun `when incrementing the score, it should increment the current score`() {
        game.incrementScore()

        assertEquals(1, game.currentScore)
    }

    @Test
    fun `when getting the next question, it should return the next question in the list`() {
        val question = Question("::question::", true)
        val game = Game(listOf(question))

        val nextQuestion = game.nextQuestion()

        assertEquals(question, nextQuestion)
    }

    @Test
    fun `when getting the next question and the end of the list has been reached, it should return null`() {

        val question = Question("::question::", true)
        val game = Game(listOf(question))

        game.nextQuestion()
        val nextQuestion = game.nextQuestion()

        assertNull(nextQuestion)
    }
}