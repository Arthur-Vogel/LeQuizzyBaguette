package com.example.quizz;

import android.os.Bundle;

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
    private String leadboardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeadborderBinding.inflate(getLayoutInflater());
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




        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leadborder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}