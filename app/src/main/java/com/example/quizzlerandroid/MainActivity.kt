package com.example.quizzlerandroid

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzlerandroid.game.viewmodel.QuizViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var questionCountTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView
    private var trueBtn: Button? = null
    private var falseBtn: Button? = null
    private val quizViewModel: QuizViewModel by viewModels { QuizViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questionTextView = findViewById(R.id.textView)
        questionCountTextView = findViewById(R.id.question_count)
        scoreTextView = findViewById(R.id.score)
        trueBtn = findViewById(R.id.button)
        falseBtn = findViewById(R.id.button2)

        quizViewModel.game().observe(this) { game ->
            questionTextView.text = game.question
            scoreTextView.text = getString(R.string.score, game.score)
            questionCountTextView.text =
                getString(R.string.question_count, game.questionCount, game.totalQuestions)
            if (game.isGameOver) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Game Over")
                alert.setCancelable(false)
                alert.setMessage("You scored " + game.score + " of " + game.totalQuestions + " points.")
                alert.setPositiveButton("Close App") { dialog, which -> finish() }
                alert.show()
            }
        }

        quizViewModel.initGame()

        trueBtn?.setOnClickListener {
            quizViewModel.answer()
        }
        falseBtn?.setOnClickListener {
            quizViewModel.answer()
        }
    }
}