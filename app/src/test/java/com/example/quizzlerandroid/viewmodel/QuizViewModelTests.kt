package com.example.quizzlerandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: QuizViewModel
    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<Boolean>
    private lateinit var quizRepository: QuizRepository
    private lateinit var gameObserver: Observer<QuizViewModel.GameState>

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher = StandardTestDispatcher())

        quizRepository = mock();
        viewModel = QuizViewModel(quizRepository)
        loadingObserver = mock()
        viewModel.loading().observeForever(loadingObserver)
        errorObserver = mock()
        viewModel.error().observeForever(errorObserver)

        gameObserver = mock()
        viewModel.game().observeForever(gameObserver)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initGame should fetch questions from the repository`() = runTest {
        viewModel.initGame()
        advanceUntilIdle()

        verify(quizRepository).getQuestions()
    }

    @Test
    fun `initGame should show loader while loading game`() {
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(true))
    }

    @Test
    fun `initGame should hide errors while loading game`() {
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
    fun `initGame should hide loader and error when successful`() = runTest {
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
    }

    @Test
    fun `the game is not over when it starts`() = runTest {
        doAnswer {
            listOf(Quiz())
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = "", isGameOver = false
                )
            )
        )
    }

    @Test
    fun `initGame should show first question when successful`() = runTest {
        val quiz = Quiz();
        quiz.question = "::question::"

        val questions = listOf(quiz)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = "::question::",
                )
            )
        )
    }

    @Test
    fun `should show score`() = runTest {
        val quiz = Quiz();
        quiz.question = "::question::"

        val questions = listOf(quiz)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = "::question::", score = 0
                )
            )
        )
    }

    @Test
    fun `should show the question count`() = runTest {
        val quiz = Quiz();
        quiz.question = "::question::"

        val questions = listOf(quiz, quiz, quiz)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = "::question::",
                    score = 0,
                    questionCount = 1,
                    totalQuestions = questions.size
                )
            )
        )
    }

    @Test
    fun `updates the question and count when answered`() = runTest {
        val quiz1 = Quiz();
        quiz1.question = "::question-1::"

        val quiz2 = Quiz();
        quiz2.question = "::question-2::"

        val quiz3 = Quiz();
        quiz3.question = "::question-3::"

        val questions = listOf(quiz1, quiz2, quiz3)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        viewModel.answer(true)

        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = quiz2.question,
                    score = 0,
                    questionCount = 2,
                    totalQuestions = questions.size,
                )
            )
        )
    }

    @Test
    fun `ends the game when the last question has been answered`() = runTest {
        val quiz1 = Quiz();
        quiz1.question = "::question-1::"

        val quiz2 = Quiz();
        quiz2.question = "::question-2::"

        val questions = listOf(quiz1, quiz2)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        viewModel.answer(true)
        viewModel.answer(true)

        verify(gameObserver, times(3)).onChanged(any())
        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = quiz2.question,
                    score = 0,
                    questionCount = 2,
                    totalQuestions = questions.size,
                    isGameOver = true
                )
            )
        )
    }

    @Test
    fun `increments the score when the answer is correct`() = runTest {
        val quiz = Quiz();
        quiz.question = "::question-1::"
        quiz.isAnswer = true

        val questions = listOf(quiz)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        viewModel.answer(true)
        advanceUntilIdle()

        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = quiz.question,
                    score = 1,
                    questionCount = 1,
                    totalQuestions = 1,
                    isGameOver = true
                )
            )
        )
    }

    @Test
    fun `does not increment the score when the answer is incorrect`() = runTest {
        val quiz = Quiz();
        quiz.question = "::question-1::"
        quiz.isAnswer = true

        val questions = listOf(quiz)
        doAnswer {
            questions
        }.whenever(quizRepository).getQuestions()

        viewModel.initGame()
        advanceUntilIdle()

        viewModel.answer(false)

        verify(gameObserver).onChanged(
            eq(
                QuizViewModel.GameState(
                    question = quiz.question,
                    score = 0,
                    questionCount = 1,
                    totalQuestions = 1,
                    isGameOver = true
                )
            )
        )
    }
}