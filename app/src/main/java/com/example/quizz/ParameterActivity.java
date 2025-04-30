package com.example.quizz;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizz.databinding.ActivityMainBinding;
import com.example.quizz.databinding.ActivityParameterBinding;

public class ParameterActivity extends AppCompatActivity {
    ActivityParameterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParameterBinding.inflate(getLayoutInflater());

        binding.deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete account
            }
        });

        binding.restartPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset point
            }
        });

        binding.renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rename user
            }
        });

        binding.lightModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change the light mode
            }
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parameter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}