package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import com.example.quizz.databinding.ActivityQuestionBinding;
import com.example.quizz.question.QuestionRepository;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuestionActivity extends AppCompatActivity {
    public static final String TOPIC_ID = "topic_id";
    public int page = 1;
    public int maxpage = 5;
    public int topicId = -1;

    private ActivityQuestionBinding binding;
    private QuestionRepository questionRepository;

    public static Intent QuestionActivityIntentFactory(Context applicationContext, int topicId){
        Intent intent = new Intent(applicationContext, QuestionActivity.class);
        intent.putExtra(TOPIC_ID, topicId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questionRepository = QuestionRepository.getRepository(getApplication());
        topicId = getIntent().getIntExtra(TOPIC_ID, -1);

        binding.textViewTopic.setText("getTopicStringByIdthatshouldbedonebyArnaud");

    }
}