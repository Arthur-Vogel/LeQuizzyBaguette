package com.example.quizz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import android.content.SharedPreferences;

import androidx.appcompat.widget.SwitchCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import com.example.quizz.databinding.ActivityParameterBinding;
import com.example.quizz.user.UserRepository;
import com.example.quizz.user.User;

public class ParameterActivity extends AppCompatActivity {

    ActivityParameterBinding binding;
    private UserRepository userRepository;
    //private LandingPage landingPage;
    private User user;
    private Switch themeSwitch;
    private SharedPreferences prefs;
    boolean isDarkMode;

    @Override
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("dark_mode", false);



        Toast.makeText(this, "ParameterActivity ouverte", Toast.LENGTH_SHORT).show();
        binding = ActivityParameterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userRepository = UserRepository.getRepository(getApplication());


        int userId = getIntent().getIntExtra("userId",-1);
        Log.d("DEBUG", "Reçu userId = " + userId);

        userRepository.getUserByUserId(userId).observe(this, user -> {
            Log.d("DEBUG", "Observe déclenché, user = " + user);
            if (user == null) {
                Toast.makeText(this, "Error loading user. Try restarting app.", Toast.LENGTH_SHORT).show();
                return;
            }
            this.user = user;
            binding.nameAndPointsTextView.setText("Name : " + this.user.username + "\n"
                    + "Score : "+ this.user.score);
            Log.d("DEBUG", "Name : " + this.user.username + "\n"
                    + "Score : "+ this.user.score);
        });

        LandingPage landingPage = LandingPage.getInstance();
        if (landingPage != null) {
            binding.deleteAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete account
                    userRepository.removeUser(user);
                    landingPage.logout(); // ca ca serit pas mal à faire mais ca m a l'aire galère d'appeler une fonction d'une autre classe
                    //load main activity
                   // startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            });

        } else {
            Log.d("ParameterActivity", "LandingActivity n'est pas encore initialisée.");
        }


        binding.restartPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.score = 0;

            }
        });

        binding.renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rename user
                String newName = binding.nameInput.getText().toString();
                if (newName.isEmpty()) {
                    // Show error message
                    return;
                }
                userRepository.renameUser(newName, user.id);

                // Update the UI or show a success message

            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load the landing page
                Intent intent = LandingPage.LandingPageIntentFactory(getApplicationContext(), user.id);
                startActivity(intent);
            }
        });

        binding.lightModeSwitch.setText("" + (isDarkMode ? "Dark Mode" : "Light Mode"));
        binding.lightModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDarkMode = !isDarkMode;



                AppCompatDelegate.setDefaultNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                prefs.edit().putBoolean("dark_mode", isDarkMode).apply();

            }
        });




        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }
    static Intent ParameterActivityIntentFactory(Context context){
        return new Intent(context, ParameterActivity.class);
    }


}