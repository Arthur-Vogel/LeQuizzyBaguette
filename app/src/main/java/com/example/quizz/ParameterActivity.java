package com.example.quizz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.quizz.databinding.ActivityMainBinding;
import com.example.quizz.databinding.ActivityParameterBinding;

public class ParameterActivity extends AppCompatActivity {
    ActivityParameterBinding binding;
    private UserRepository userRepository;
    private LandingPage landingPage;
    private User user;

    @Override
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "ParameterActivity ouverte", Toast.LENGTH_SHORT).show();
        binding = ActivityParameterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userRepository = UserRepository.getRepository(getApplication());
        //landingPage = LandingPage.inflate(getApplicationContext(), userRepository);

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






        binding.deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete account
                userRepository.removeUser(user);
                landingPage.logout();

            }
        });

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

        binding.lightModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change the light mode
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