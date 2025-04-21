package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.databinding.ActivityLandingPageBinding;

public class LandingPage extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.myapplication.MAIN_ACTIVITY_USER_ID";
    ActivityLandingPageBinding binding;

    private static final int LOGGED_OUT = -1;
    private int loggedInUserId = -1;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (user.isAdmin){
            binding.AdminAreaButton.setVisibility(View.VISIBLE);
            binding.AdminAreaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else { binding.AdminAreaButton.setVisibility(View.INVISIBLE);}

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_user_id_key), loggedInUserId);
        sharedPrefEditor.apply();
    }
}