package com.example.quizz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public static Intent loginIntentFactory(Context context){
        return new Intent(context, LoginActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = UserRepository.getRepository(getApplication());

        if (isAlreadyLogged()){ return; }

        binding.loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                verifyUser();
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

    private void verifyUser() {

        String username = binding.usernameInput.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, "Username may not be blank.", Toast.LENGTH_SHORT).show();
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passwordInput.getText().toString();
                if (password.equals(user.password)) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(LandingPage.LandingPageIntentFactory(getApplicationContext(), user.id));
                } else {
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}