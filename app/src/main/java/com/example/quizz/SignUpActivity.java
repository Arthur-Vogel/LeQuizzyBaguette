package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.quizz.databinding.ActivityLoginBinding;
import com.example.quizz.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private UserRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = UserRepository.getRepository(getApplication());


        binding.signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createUser();
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void createUser() {

        String username = binding.usernameInput.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(this, "Username may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);

        userObserver.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // If the user does not exist in the database
                if (user == null) {
                    String password = binding.passwordInput.getText().toString();

                    if (isPasswordValid(password)) {
                        // Create a new User and insert it into the database
                        User testUser = new User(username, password);
                        testUser.isAdmin = false;

                        repository.insertUser(testUser);

                        // Login screen
                        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
                    } else {
                        Toast.makeText(SignUpActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }

                    // Remove the observer after it has been triggered once
                    userObserver.removeObserver(this);
                } else {
                    // error message if username already used
                    Toast.makeText(SignUpActivity.this, "Username already used", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isPasswordValid(String password) {
        return !(password.equals("nathan") || password.equals("arthur"));
    }


    static Intent signupIntentFactory(Context context){
        return new Intent(context, SignUpActivity.class);
    }
}