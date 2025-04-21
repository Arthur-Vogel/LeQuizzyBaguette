package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        binding.loginButton.setOnClickListener(v -> {
            String username = binding.usernameInput.getText().toString();
            String password = binding.passwordInput.getText().toString();

            executor.execute(() -> {
                User user = repository.getUserByUsernameAndPassword(username, password);
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                        // Proceed to next activity if needed
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }
}