package com.example.quizz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizz.databinding.ActivityLoginBinding;
import com.example.quizz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (isAlreadyLogged()){ return; }

        binding.LeftLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
            }
        });
        binding.RightCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Replace with create user activity here
                startActivity(SignUpActivity.signupIntentFactory(getApplicationContext()));
            }
        });
    }

    private boolean isAlreadyLogged(){

        // Test if user is still logged in from previous sessions
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_user_id_key), -1);
        if (loggedInUserId != -1){
            startActivity(LandingPage.LandingPageIntentFactory(getApplicationContext(), loggedInUserId));
            return true;
        }
        return false;
    }
}