package com.example.quizzlerandroid.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzlerandroid.Data
import com.example.quizzlerandroid.Quiz
import com.example.quizzlerandroid.api.QuizApi
import com.example.quizzlerandroid.R
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
    private var quizzes: List<Quiz>? = null
    private var score = 0
    private var quizIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questionTextView = findViewById(R.id.textView)
        scoreTextView = findViewById(R.id.score)
        trueBtn = findViewById(R.id.button)
        falseBtn = findViewById(R.id.button2)
        scoreTextView?.text = "Score $score/10"

        // do View model stuff



        try {
            quiz
        } catch (e: IOException) {
            e.printStackTrace()
        }
        trueBtn?.setOnClickListener(View.OnClickListener {
            checkAnswer(true)
            updateQuestion()
        })
        falseBtn?.setOnClickListener(View.OnClickListener {
            checkAnswer(false)
            updateQuestion()
        })
    }

//    @get:Throws(IOException::class)
//    private val quiz: Unit
//        private get() {
//            val retrofit = Retrofit.Builder()
//                .baseUrl(QuizApi.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            val quizApi = retrofit.create(QuizApi::class.java)
//            val call = quizApi.quiz()
//            call.enqueue(object : Callback<Data?> {
//                override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
//                    val data = response.body()!!
//                    quizzes = data.data
//                    play(quizzes)
//                }
//
//                override fun onFailure(call: Call<Data?>, t: Throwable) {
//                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
//                }
//            })
//        }

    private fun play(questions: List<Quiz>?) {
        val question = questions!![quizIndex]
        questionTextView!!.text = question.question
    }

    private fun updateQuestion() {
        quizIndex = (quizIndex + 1) % quizzes!!.size
        if (quizIndex == 0) {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Game Over")
            alert.setCancelable(false)
            alert.setMessage("You scored " + score + " points in " + quizzes!!.size + " questions")
            alert.setPositiveButton("Close App") { dialog, which -> finish() }
            alert.show()
        }
        val question = quizzes!![quizIndex]
        questionTextView!!.text = question.question
        scoreTextView!!.text = "Score " + score + "/" + quizzes!!.size
    }

    private fun checkAnswer(answer: Boolean) {
        val correctAnswer = quizzes!![quizIndex].isAnswer
        if (correctAnswer == answer) {
            score++
        }
    }
}