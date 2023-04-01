package com.example.quizzlerandroid

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzlerandroid.databinding.ActivityMainBinding
import com.example.quizzlerandroid.game.viewmodel.QuizViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels { QuizViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        quizViewModel.game().observe(this) { game ->
            binding.question.text = game.question
            binding.score.text = getString(R.string.score, game.score)
            binding.questionCount.text =
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

        binding.btnTrue.setOnClickListener {
            quizViewModel.answer(true)
        }
        binding.btnFalse.setOnClickListener {
            quizViewModel.answer(false)
        }
    }
}