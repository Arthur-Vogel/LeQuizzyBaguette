package com.example.quizz;

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

import com.example.quizz.databinding.ActivityLeadborderBinding;
import com.example.quizz.databinding.ActivityParameterBinding;

import java.util.List;

public class LeadborderActivity extends AppCompatActivity {

    ActivityLeadborderBinding binding;
    private UserRepository userRepository;
    private User user;
    private String leadboardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeadborderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userRepository = UserRepository.getRepository(getApplication());

        leadboardText = "";

        // take all the users in the database and take the 10th best score

        LiveData<List<User>> usersObserver = userRepository.getAllUsers();
        usersObserver.observe(this, users -> {

            users.sort((user1, user2) -> Integer.compare(user2.score, user1.score));
            List<User> topUsers = users.subList(0, Math.min(users.size(), 10));
            if(topUsers.isEmpty()){
                leadboardText = "No users";
            }



            for (User user : topUsers) {
                leadboardText += user.username + " : " + user.score + "\n";
            }
            binding.leadboard.setText(leadboardText);
        });



        binding.leadboard.setText(leadboardText);

        int userId = getIntent().getIntExtra("userId",-1);
        Log.d("DEBUG", "Reçu userId = " + userId);

        userRepository.getUserByUserId(userId).observe(this, user -> {
            Log.d("DEBUG", "Observe déclenché, user = " + user);
            if (user == null) {
                Toast.makeText(this, "Error loading user. Try restarting app.", Toast.LENGTH_SHORT).show();
                return;
            }
            this.user = user;
            binding.leaderboardBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //load the landing page
                    Intent intent = LandingPage.LandingPageIntentFactory(getApplicationContext(), user.id);
                    startActivity(intent);
                }
            });
        });






        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}