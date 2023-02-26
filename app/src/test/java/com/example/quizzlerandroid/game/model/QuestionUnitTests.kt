package com.example.quizzlerandroid.game.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class QuestionUnitTests {

    private lateinit var question: Question

    @Before
    fun setup(){
        question = Question("::question::", true)
    }

    @Test
    fun `when creating question, it should not have an answered option`(){
        assertNull(question.answeredOption)
    }

    @Test
    fun `when answering, it should have answered option`(){
        question.answer(true)

        assertEquals(true,question.answeredOption)
    }

    @Test
    fun `when answering with the correct option, should return true`(){
        val result = question.answer(true)

        assertTrue(result)
    }

    @Test
    fun `when answering with the incorrect option, should return false`(){
        val result = question.answer(false)

        assertFalse(result)
    }
}