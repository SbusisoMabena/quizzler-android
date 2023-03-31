package com.example.quizzlerandroid

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzlerandroid.common.network.QuizApi
import com.example.quizzlerandroid.game.data.Quiz
import com.example.quizzlerandroid.game.viewmodel.QuizViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var questionTextView: TextView? = null
    private var scoreTextView: TextView? = null
    private var trueBtn: Button? = null
    private var falseBtn: Button? = null
    private lateinit var quizzes: List<Quiz>
    private var score = 0
    private var quizIndex = 0
    private val quizViewModel: QuizViewModel by viewModels { QuizViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questionTextView = findViewById(R.id.textView)
        scoreTextView = findViewById(R.id.score)
        trueBtn = findViewById(R.id.button)
        falseBtn = findViewById(R.id.button2)
        scoreTextView?.text = "Score $score/10"

        quizViewModel.questions().observe(this, Observer {
            play(it)
            quizzes = it
        })
        quizViewModel.initGame()

        trueBtn?.setOnClickListener(View.OnClickListener {
            checkAnswer(true)
            updateQuestion()
        })
        falseBtn?.setOnClickListener(View.OnClickListener {
            checkAnswer(false)
            updateQuestion()
        })
    }

    private fun play(questions: List<Quiz>?) {
        val question = questions!![quizIndex]
        questionTextView!!.text = question.question
    }

    private fun updateQuestion() {
        quizIndex = (quizIndex + 1) % quizzes.size
        if (quizIndex == 0) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Game Over")
            alert.setCancelable(false)
            alert.setMessage("You scored " + score + " points in " + quizzes.size + " questions")
            alert.setPositiveButton("Close App") { dialog, which -> finish() }
            alert.show()
        }
        val question = quizzes[quizIndex]
        questionTextView!!.text = question.question
        scoreTextView!!.text = "Score " + score + "/" + quizzes.size
    }

    private fun checkAnswer(answer: Boolean) {
        val correctAnswer = quizzes[quizIndex].isAnswer
        if (correctAnswer == answer) {
            score++
        }
    }
}