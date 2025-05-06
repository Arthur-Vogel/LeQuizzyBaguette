package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.quizz.databinding.ActivityChooseTypeBinding;

public class ChooseTypeActivity extends AppCompatActivity {

    ActivityChooseTypeBinding binding;

    String[] topics = { "Food", "Culture", "Geography", "History", "Famous People" };
    String[] difficulties = { "Easy", "Normal", "Hard", "Impossible" };
    AutoCompleteTextView topicDropDown;
    AutoCompleteTextView difficultyDropdown;
    ArrayAdapter<String> topicAdapter;
    ArrayAdapter<String> difficultyAdapter;

    public static Intent chooseTypeIntentFactory(Context context) {
        return new Intent(context, ChooseTypeActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topicDropDown = binding.topicInput;
        topicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, topics);
        topicDropDown.setAdapter(topicAdapter);

        difficultyDropdown = binding.difficultyInput;
        difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, difficulties);
        difficultyDropdown.setAdapter(difficultyAdapter);

        topicDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(ChooseTypeActivity.this, "Topic : " + item, Toast.LENGTH_SHORT).show();
            }
        });

        difficultyDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(ChooseTypeActivity.this, "Difficulty : " + item, Toast.LENGTH_SHORT).show();
            }
        });

        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(QuestionActivity.QuestionIntentFactory(getApplicationContext()));
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