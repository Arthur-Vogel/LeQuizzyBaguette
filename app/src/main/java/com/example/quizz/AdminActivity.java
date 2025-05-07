package com.example.quizz;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizz.answer.Answer;
import com.example.quizz.answer.AnswerDAO;
import com.example.quizz.answer.AnswerRepository;
import com.example.quizz.databinding.ActivityAdminBinding;
import com.example.quizz.databinding.ActivityLoginBinding;
import com.example.quizz.databinding.ActivitySignUpBinding;
import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionDAO;
import com.example.quizz.question.QuestionRepository;
import com.example.quizz.user.UserRepository;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private UserRepository repository;

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
    }

    private void createQuestion() {
        String question = binding.questionInput.getText().toString();
        String correct_answer = binding.correctAnswer.getText().toString();
        String wrong_answer1 = binding.answerWrong1.getText().toString();
        String wrong_answer2 = binding.answerWrong2.getText().toString();
        String wrong_answer3 = binding.answerWrong3.getText().toString();
        String theme = "";
        RadioGroup radioGroup = findViewById(R.id.foodChoices);
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            theme = selectedRadioButton.getText().toString();
        }
        if (question.isEmpty()) {
            Toast.makeText(this, "Question may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (correct_answer.isEmpty() || wrong_answer1.isEmpty() || wrong_answer2.isEmpty() || wrong_answer3.isEmpty()) {
            Toast.makeText(this, "Answer may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (theme.isEmpty()) {
            Toast.makeText(this, "Theme may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "You added " + question + " to " + theme  , Toast.LENGTH_SHORT).show();
        Question question1 = new Question(question, 3, 0, 100);
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