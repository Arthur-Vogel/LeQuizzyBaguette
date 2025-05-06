package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.quizz.databinding.ActivityChooseTypeBinding;

public class ChooseTypeActivity extends AppCompatActivity {

    ActivityChooseTypeBinding binding;

    public static Intent chooseTypeIntentFactory(Context context){
        return new Intent(context, ChooseTypeActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}