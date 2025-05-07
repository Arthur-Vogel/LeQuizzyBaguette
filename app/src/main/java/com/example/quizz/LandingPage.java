package com.example.quizz;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizz.databinding.ActivityLandingPageBinding;
import com.example.quizz.user.User;
import com.example.quizz.user.UserRepository;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LandingPage extends AppCompatActivity {

    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.quizz.SAVED_INSTANCE_STATE_USERID_KEY";
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.quizz.MAIN_ACTIVITY_USER_ID";

    public static final String TAG = "DAC_LEQUIZZYBAGUETTE";
    ActivityLandingPageBinding binding;
    private UserRepository repository;


    private static final int LOGGED_OUT = -1;
    private int loggedInUserId = -1;
    private static LandingPage instance;

    private User user;

    public static Intent LandingPageIntentFactory(Context applicationContext, int userId) {
        Intent intent = new Intent(applicationContext, LandingPage.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }
    public static LandingPage getInstance() {
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        instance = this;

        repository = UserRepository.getRepository(getApplication());
        if (repository == null){
            Log.println(Log.ERROR, TAG, "Repository null");
        }
        loginUser(savedInstanceState);

        // User not logged in
        if (loggedInUserId == -1){
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });


        binding.playButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        }));

    }


    private void play() {
        startActivity(ChooseTypeActivity.chooseTypeIntentFactory(getApplicationContext(), loggedInUserId));


    }

    public void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
/*
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LandingPage.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }*/


    private void loginUser(Bundle savedInstanceState) {
        Log.i(TAG, "loginUser function started");

        //loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_user_id_key), LOGGED_OUT);

        if (loggedInUserId == LOGGED_OUT && savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            Log.i(TAG, "Restoring user ID from savedInstanceState");
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }

        if (loggedInUserId == LOGGED_OUT) {
            Log.i(TAG, "Getting user ID from intent extras");
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }

        if (loggedInUserId == LOGGED_OUT) {
            Log.e(TAG, "User ID still not found. Redirecting to login screen.");
            startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
            return;
        }

        // Now observe the user LiveData — this ensures we react once the user is actually loaded
        repository.getUserByUserId(loggedInUserId).observe(this, user -> {
            if (user == null) {
                Log.e(TAG, "User not found in DB. Likely DB not initialized yet.");
                Toast.makeText(this, "Error loading user. Try restarting app.", Toast.LENGTH_SHORT).show();
                return;
            }

            this.user = user;
            Log.i(TAG, "User loaded: " + user.username);
            //invalidateOptionsMenu();

            // Optional: show a message or update UI
            updateSharedPreference();
            checkIfAdmin(user);
            binding.UsernameTextView.setText(user.username);
            binding.ScoreTextView.setText("Score: " + user.score);


            binding.parameterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //start Parameter Activity
                    //Intent intent = ParameterActivity.ParameterActivityIntentFactory(getApplicationContext());
                    Intent intent = new Intent(LandingPage.this, ParameterActivity.class);
                    intent.putExtra("userId", user.id);
                    startActivity(intent);
                }
            });
            binding.leaderboardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start Leaderboard Activity
                    Intent intent = new Intent(LandingPage.this, LeadborderActivity.class);
                    intent.putExtra("userId", user.id);
                    startActivity(intent);
                }
            });



        });


    }

    private void checkIfAdmin(User user) {
        Log.println(Log.ERROR, TAG, "user" + user.isAdmin + user.username);
        if (user.isAdmin) {
            binding.AdminAreaButton.setVisibility(View.VISIBLE);
            binding.AdminAreaButton.setOnClickListener(v -> {
                // Handle admin button
            });
        } else {
            binding.AdminAreaButton.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_user_id_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreference();
    }


    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LandingPage.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }




}