package com.example.quizz;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizz.answer.Answer;
import com.example.quizz.answer.AnswerDAO;
import com.example.quizz.answer.AnswerRepository;
import com.example.quizz.databinding.ActivityAdminBinding;
import com.example.quizz.databinding.ActivityLoginBinding;
import com.example.quizz.databinding.ActivitySignUpBinding;
import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionDAO;
import com.example.quizz.question.QuestionRepository;
import com.example.quizz.topic.Topic;
import com.example.quizz.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private UserRepository repository;

    AutoCompleteTextView topicDropDown;
    ArrayAdapter<String> topicAdapter;



    private QuestionRepository questionRepository;

    private AnswerRepository answerRepository;

    private static volatile AppDatabase INSTANCE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = UserRepository.getRepository(getApplication());
        questionRepository = QuestionRepository.getRepository(getApplication());
        answerRepository = AnswerRepository.getRepository(getApplication());

        topicDropDown = binding.extraTopicInput;
        ChooseTypeViewModel viewModel = new ViewModelProvider(this).get(ChooseTypeViewModel.class);

        viewModel.allTopics.observe(this, topicList -> {
            List<String> topics = new ArrayList<>();
            topics.add("Random");

            for (Topic topic : topicList) {
                topics.add(topic.getTopic());
            }
            ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, topics);
            topicDropDown.setAdapter(topicAdapter);
        });


        binding.submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createQuestion();
            }
        });
        binding.goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
            }
        });
        topicDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(AdminActivity.this, "Topic : " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createQuestion() {
        String question = binding.questionInput.getText().toString();
        String correct_answer = binding.correctAnswer.getText().toString();
        String wrong_answer1 = binding.answerWrong1.getText().toString();
        String wrong_answer2 = binding.answerWrong2.getText().toString();
        String wrong_answer3 = binding.answerWrong3.getText().toString();
        String theme = topicDropDown.getText().toString();
        int id;
        if (question.isEmpty()) {
            Toast.makeText(this, "Question may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (correct_answer.isEmpty() || wrong_answer1.isEmpty() || wrong_answer2.isEmpty() || wrong_answer3.isEmpty()) {
            Toast.makeText(this, "Answer may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (theme) {
            case "Culture":
                id = 0;
                break;
            case "Food":
                id = 1;
                break;
            case "History":
                id = 2;
                break;
            case "Geography":
                id = 3;
                break;
            case "Famous People":
                id = 4;
                break;
            default:
                id = 0;
                break;
        }
        Toast.makeText(this, "You added " + question + " to " + theme  , Toast.LENGTH_SHORT).show();
        Question question1 = new Question(question, 3, id, 100);
        questionRepository.insertQuestion(question1);
        Answer answer1 = new Answer(question1.answerListId, correct_answer, true);
        answerRepository.insert(answer1);
        Answer answer2 = new Answer(question1.answerListId, wrong_answer1, false);
        answerRepository.insert(answer2);
        Answer answer3 = new Answer(question1.answerListId, wrong_answer2, false);
        answerRepository.insert(answer3);
        Answer answer4 = new Answer(question1.answerListId, wrong_answer3, false);
        answerRepository.insert(answer4);

    }

    static Intent adminIntentFactory(Context context){
        return new Intent(context, AdminActivity.class);
    }
}