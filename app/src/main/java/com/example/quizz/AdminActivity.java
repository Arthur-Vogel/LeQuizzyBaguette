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

import com.example.quizz.databinding.ActivityAdminBinding;
import com.example.quizz.databinding.ActivityLoginBinding;
import com.example.quizz.databinding.ActivitySignUpBinding;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private UserRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = UserRepository.getRepository(getApplication());


        binding.submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createQuestion();
            }
        });
    }

    private void createQuestion() {
        String question = binding.questionInput.getText().toString();
        String answer = binding.answerInput.getText().toString();
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

        if (answer.isEmpty()) {
            Toast.makeText(this, "Answer may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (theme.isEmpty()) {
            Toast.makeText(this, "Theme may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "You added" + theme + question , Toast.LENGTH_SHORT).show();

    }

    static Intent adminIntentFactory(Context context){
        return new Intent(context, AdminActivity.class);
    }
}