package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.quizz.User;
import com.example.quizz.UserRepository;
import com.example.quizz.databinding.ActivityLoginBinding;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserRepository repository;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = UserRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                verifyUser();
            }
        });
    }

    private void verifyUser() {

        String username = binding.usernameInput.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(this, "Username may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null){
                String password = binding.passwordInput.getText().toString();
                if (password.equals(user.password)){
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(LandingPage.LandingPageIntentFactory(getApplicationContext(), user.id));
                } else {
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static Intent loginIntentFactory(Context context){
        return new Intent(context, LoginActivity.class);
    }
}