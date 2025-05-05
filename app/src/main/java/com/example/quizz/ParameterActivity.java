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
    private UserRepository userRepository;
    //private User user;

    //@Override
    protected void onCreate(Bundle savedInstanceState, User user) {
        super.onCreate(savedInstanceState);
        binding = ActivityParameterBinding.inflate(getLayoutInflater());
        userRepository = UserRepository.getRepository(getApplication());


        binding.deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete account
                userRepository.removeUser(user);

            }
        });

        binding.restartPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.score = 0; // NE MARCHE PAS NE CHANGE PAS DE VALEUR DANS UNE DB!!!!!

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
               UserRepository.renameUser(newName, user.id, userRepository.userDAO);
                // Update the UI or show a success message
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