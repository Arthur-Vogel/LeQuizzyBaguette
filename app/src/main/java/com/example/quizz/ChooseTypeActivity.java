package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizz.databinding.ActivityChooseTypeBinding;
import com.example.quizz.topic.Topic;

import java.util.ArrayList;
import java.util.List;

public class ChooseTypeActivity extends AppCompatActivity {
    private static final String USER_ID = "com.example.quizz.USER_ID";
    private int userId;
    ActivityChooseTypeBinding binding;

    String[] difficulties = { "Random", "Easy", "Normal", "Hard", "Impossible" };
    AutoCompleteTextView topicDropDown;
    AutoCompleteTextView difficultyDropdown;
    ArrayAdapter<String> topicAdapter;
    ArrayAdapter<String> difficultyAdapter;

    public static Intent chooseTypeIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, ChooseTypeActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId = getIntent().getIntExtra(USER_ID, -1);

        ChooseTypeViewModel viewModel = new ViewModelProvider(this).get(ChooseTypeViewModel.class);

        topicDropDown = binding.topicInput;
        viewModel.allTopics.observe(this, topicList -> {
            List<String> topics = new ArrayList<>();
            topics.add("Random");

            for (Topic topic : topicList) {
                topics.add(topic.getTopic());
            }
            ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, topics);
            topicDropDown.setAdapter(topicAdapter);
        });

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
                String selectedTopic = topicDropDown.getText().toString();
                String selectedDifficulty = difficultyDropdown.getText().toString();

                startActivity(QuestionActivity.QuestionActivityIntentFactory(getApplicationContext(),
                        selectedTopic, 5, userId));
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