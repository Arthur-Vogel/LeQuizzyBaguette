package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizz.databinding.ActivityResultBinding;


public class ResultActivity extends AppCompatActivity {
    ActivityResultBinding binding;

    private static final String TOTAL_QUESTIONS = "com.example.quizz.TOTAL_QUESTIONS";
    private static final String CORRECT_ANSWERS = "com.example.quizz.CORRECT_ANSWERS";
    private static final String FINAL_SCORE = "com.example.quizz.FINAL_SCORE";
    private static final String BEST_SCORE = "com.example.quizz.BEST_SCORE";

    public static Intent ResultIntentFactory(Context applicationContext, int totalQuestions, int correctAnswers, int finalScore, int bestScore) {
        Intent intent = new Intent(applicationContext, ResultActivity.class);
        intent.putExtra(TOTAL_QUESTIONS, totalQuestions);
        intent.putExtra(CORRECT_ANSWERS, correctAnswers);
        intent.putExtra(FINAL_SCORE, finalScore);
        intent.putExtra(BEST_SCORE, bestScore);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String tqText = "Total Questions : " + getIntent().getIntExtra(TOTAL_QUESTIONS, 10);
        binding.totalQuestions.setText(tqText);
        String caText = "Correct Answers : " + getIntent().getIntExtra(CORRECT_ANSWERS, 0);
        binding.correctAnswers.setText(caText);
        String fsText = "Final Score : " + getIntent().getIntExtra(FINAL_SCORE, 0);
        binding.finalScore.setText(fsText);
        String bsText = "Best Score : " + getIntent().getIntExtra(BEST_SCORE, 0);
        binding.bestScore.setText(bsText);


        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseTypeActivity.class));
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LandingPage.class));
            }
        });
    }
}