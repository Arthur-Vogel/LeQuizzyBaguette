package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizz.databinding.ActivityQuestionBinding;

public class QuestionActivity extends AppCompatActivity {

    ActivityQuestionBinding binding;

    public static Intent QuestionIntentFactory(Context context) {
        return new Intent(context, QuestionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}