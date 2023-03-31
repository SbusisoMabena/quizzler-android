package com.example.quizzlerandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.common.network.QuizApi
import com.example.quizzlerandroid.common.repository.QuizRepository
import com.example.quizzlerandroid.game.data.Quiz
import com.example.quizzlerandroid.game.viewmodel.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.internal.verification.Calls
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Retrofit
import javax.security.auth.callback.Callback

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var api: QuizApi
    private lateinit var viewModel: QuizViewModel
    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<Boolean>
    private lateinit var questionsObserver: Observer<List<Quiz>>
    private lateinit var quizRepository: QuizRepository

    @Before
    fun setup() {
        quizRepository = mock();
        viewModel = QuizViewModel(quizRepository)
        loadingObserver = mock()
        viewModel.loading().observeForever(loadingObserver)
        errorObserver = mock()
        viewModel.error().observeForever(errorObserver)
        questionsObserver = mock()
        viewModel.questions().observeForever(questionsObserver)
        Dispatchers.setMain(dispatcher = StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initGame should show loading`() {
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(true))
    }

    @Test
    fun `initGame should hide errors`() {
        viewModel.initGame()
        verify(errorObserver).onChanged(eq(false))
    }

    @Test
    fun `initGame returns an error response and turns off the loader when it fails to fetch questions`() =
        runTest(UnconfinedTestDispatcher()) {
            doAnswer {
                throw Exception()
            }.whenever(quizRepository).getQuestions()

            viewModel.initGame()
            advanceUntilIdle()

            verify(errorObserver).onChanged(eq(true))
            verify(loadingObserver, times(2)).onChanged(eq(false))
        }

    @Test
    fun `initGame should fetch questions from the repository`() = runTest {
        val quiz = Quiz();
        quiz.question = "::question::"

        val questions = listOf(quiz)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()
        viewModel.initGame()
        advanceUntilIdle()

        verify(errorObserver).onChanged(eq(false))
        verify(loadingObserver).onChanged(eq(false))
        verify(questionsObserver).onChanged(eq(questions))
    }
}