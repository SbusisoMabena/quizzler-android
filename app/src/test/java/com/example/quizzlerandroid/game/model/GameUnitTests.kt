package com.example.quizzlerandroid.game.model

import org.junit.Assert
import org.junit.Test

class GameUnitTests {
    @Test
    fun `when incrementing the score, it should increment the current score`() {
        val game = Game()

        game.incrementScore()

        Assert.assertEquals(1, game.currentScore)
    }
}