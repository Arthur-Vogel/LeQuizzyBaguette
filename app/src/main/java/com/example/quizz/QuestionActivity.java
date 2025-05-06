package com.example.quizz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import com.example.quizz.answer.Answer;
import com.example.quizz.answer.AnswerRepository;
import com.example.quizz.databinding.ActivityQuestionBinding;
import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionRepository;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    public static final String TOPIC_ID = "topic_id";
    public static final String PAGE_MAX = "page_max";
    public int page = 1;
    public int maxpage = 5;
    public int topicId = -1;

    private ActivityQuestionBinding binding;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;

    private List<Question> questionsList;
    private Question question;
    private List<Answer> answerList;

    public static Intent QuestionActivityIntentFactory(Context applicationContext, int topicId, int nb_question_u_want){
        Intent intent = new Intent(applicationContext, QuestionActivity.class);
        intent.putExtra(TOPIC_ID, topicId);
        intent.putExtra(PAGE_MAX, nb_question_u_want);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Repo
        questionRepository = QuestionRepository.getRepository(getApplication());
        answerRepository = AnswerRepository.getRepository(getApplication());

        // Topic
        topicId = getIntent().getIntExtra(TOPIC_ID, -1);
        String topic = "Topic: " + topicId;
        binding.textViewTopic.setText(topic);

        // Page count
        binding.textViewPage.setText(page);
        maxpage = getIntent().getIntExtra(PAGE_MAX, 5);
        binding.textViewPageMax.setText(maxpage);

        // Question
        questionsList = questionRepository.getQuestionByTopicId(topicId);
        if (!questionsList.isEmpty()) {
            question = questionsList.get(new Random().nextInt(questionsList.size()));
        }

        // Answers
        answerList = answerRepository.getAnswerListByQuestionId(question.id);
        if (answerList.size() != 4){
            Toast.makeText(this, "AnswerList is not size of 4!", Toast.LENGTH_SHORT).show();
        }
        Collections.shuffle(answerList);

        updateView();

        // Buttons onClick
        binding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        binding.buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(0).isCorrect;
                if (isCorrect){
                    nextQuestion();
                    // Add points to player
                }
                else {
                    // Points that can be gained are reduced
                }
            }
        });

        binding.buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(1).isCorrect;
                if (isCorrect){
                    nextQuestion();
                    // Add points to player
                }
                else {
                    // Points that can be gained are reduced
                }
            }
        });

        binding.buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(2).isCorrect;
                if (isCorrect){
                    nextQuestion();
                    // Add points to player
                }
                else {
                    // Points that can be gained are reduced
                }
            }
        });

        binding.buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(3).isCorrect;
                if (isCorrect){
                    nextQuestion();
                    // Add points to player
                }
                else {
                    // Points that can be gained are reduced
                }
            }
        });
    }

    private void nextQuestion(){
        if (page < maxpage) {
            if (questionsList.size() <= 1){
                Toast.makeText(this, "No more questions to ask!", Toast.LENGTH_SHORT).show();
                //startActivity();
                return;
            }

            questionsList.remove(question);
            question = questionsList.get(new Random().nextInt(questionsList.size()));

            answerList = answerRepository.getAnswerListByQuestionId(question.id);
            if (answerList.size() != 4){
                Toast.makeText(this, "AnswerList is not size of 4!", Toast.LENGTH_SHORT).show();
            }
            Collections.shuffle(answerList);

            page += 1;
            binding.textViewPage.setText(page);

            updateView();
        }
        else {
            //startActivity();
        }
    }

    private void updateView(){
        binding.textViewQuestion.setText(question.getQuestion());
        binding.buttonAnswer1.setText(answerList.get(0).answer);
        binding.buttonAnswer2.setText(answerList.get(1).answer);
        binding.buttonAnswer3.setText(answerList.get(2).answer);
        binding.buttonAnswer4.setText(answerList.get(3).answer);
    }
}