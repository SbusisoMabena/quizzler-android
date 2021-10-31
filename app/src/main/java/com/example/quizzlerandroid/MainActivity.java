package com.example.quizzlerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testandroidapp.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView scoreTextView;
    private Button trueBtn;
    private Button falseBtn;
    private List<Quiz> quizzes;
    private int score = 0;
    private int quizIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionTextView = findViewById(R.id.textView);
        scoreTextView = findViewById(R.id.score);
        trueBtn = findViewById(R.id.button);
        falseBtn = findViewById(R.id.button2);
        scoreTextView.setText("Score " + score + "/" + 10);

        try {
            getQuiz();
        } catch (IOException e) {
            e.printStackTrace();
        }

        trueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
                updateQuestion();
            }
        });

        falseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                updateQuestion();
            }
        });
    }

    private void getQuiz() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(QuizApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QuizApi quizApi = retrofit.create(QuizApi.class);

        Call<Data> call = quizApi.getQuiz();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                Data data = response.body();
                assert data != null;
                quizzes = data.getData();
                play(quizzes);
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void play(List<Quiz> questions) {
        Quiz question = questions.get(quizIndex);

        questionTextView.setText(question.getQuestion());

    }

    private void updateQuestion() {
        quizIndex = (quizIndex + 1) % quizzes.size();
        if (quizIndex == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Game Over");
            alert.setCancelable(false);
            alert.setMessage("You scored " + score + " points in " + quizzes.size() + " questions");
            alert.setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }
        Quiz question = quizzes.get(quizIndex);
        questionTextView.setText(question.getQuestion());
        scoreTextView.setText("Score " + score + "/" + quizzes.size());
    }

    private void checkAnswer(boolean answer) {
        boolean correctAnswer = quizzes.get(quizIndex).isAnswer();
        if (correctAnswer == answer) {
            score++;
        }
    }

}