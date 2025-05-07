package com.example.quizz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;

import com.example.quizz.answer.Answer;
import com.example.quizz.answer.AnswerRepository;
import com.example.quizz.databinding.ActivityQuestionBinding;
import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionRepository;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.example.quizz.databinding.ActivityQuestionBinding;
import com.example.quizz.topic.Topic;
import com.example.quizz.topic.TopicRepository;
import com.example.quizz.user.User;
import com.example.quizz.user.UserRepository;

public class QuestionActivity extends AppCompatActivity {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    public static final String TOPIC_NAME = "com.example.quizz.TOPIC_NAME";
    public static final String PAGE_MAX = "com.example.quizz.PAGE_MAX";
    private static final String USER_ID = "com.example.quizz.USER_ID";
    public int page = 1;
    public int maxpage = 5;
    public int topicId = -1;
    public int userId;
    public Topic topic;

    private ActivityQuestionBinding binding;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private UserRepository userRepository;
    private TopicRepository topicRepository;

    private List<Question> questionsList;
    private Question question;
    private List<Answer> answerList;
    private User user;

    public static Intent QuestionActivityIntentFactory(Context applicationContext, String topicName, int nb_question_u_want, int userId) {
        Intent intent = new Intent(applicationContext, QuestionActivity.class);
        intent.putExtra(TOPIC_NAME, topicName);
        intent.putExtra(PAGE_MAX, nb_question_u_want);
        intent.putExtra(USER_ID, userId);
        return intent;
    }


    public static Intent QuestionIntentFactory(Context context) {
        return new Intent(context, QuestionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Repo
        questionRepository = QuestionRepository.getRepository(getApplication());
        answerRepository = AnswerRepository.getRepository(getApplication());
        userRepository = UserRepository.getRepository(getApplication());
        topicRepository = TopicRepository.getRepository(getApplication());
        userId = getIntent().getIntExtra(USER_ID, -1);
        if (userId == -1)
            Toast.makeText(this, "User ID not found in QuestionActivity", Toast.LENGTH_SHORT).show();
        else {
            Log.i(LandingPage.TAG, "----- User id: ----- " + userId);
            // Get user in the user variable
            userRepository.getUserByUserId(userId).observe(this, user -> {
                if (user == null) {
                    Toast.makeText(this, "Error loading user. Try restarting app.", Toast.LENGTH_SHORT).show();
                    return;
                }

                this.user = user;
                String score = user.id + "points";
                binding.textViewScore.setText(score);
            });
        }

        // Topic
//        Callable<Topic> topicTask = () -> topicRepository.getTopicFromName(getIntent().getStringExtra(TOPIC_NAME));
//        Future<Topic> topicFuture = executor.submit(topicTask);
//        try {
//            topic = topicFuture.get();
//            if (topic == null){
//                Log.e(LandingPage.TAG, "Error TOPIC selection");
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        topicId = topic.getId();
        topicId = 0;
        String topic_str = "Topic: " + topicId;
        binding.textViewTopic.setText(topic_str);
        Log.i(LandingPage.TAG, "---------- Topic id: ---------- " + topicId);

        // Page count
        String page_str = page + "";
        binding.textViewPage.setText(page_str);
        maxpage = getIntent().getIntExtra(PAGE_MAX, 5);
        String maxpage_str = maxpage + "";
        binding.textViewPageMax.setText(maxpage_str);

        // Question
        // Get the List of Question in the questionList variable
        Callable<List<Question>> questionTask = () -> questionRepository.getQuestionByTopicId(topicId);
        Future<List<Question>> questionFuture = executor.submit(questionTask);
        try {
            questionsList = questionFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        question = questionsList.get(new Random().nextInt(questionsList.size()));

        // Answer
        Callable<List<Answer>> answerTask = () -> answerRepository.getAnswerListByQuestionId(question.answerListId);
        Future<List<Answer>> answerFuture = executor.submit(answerTask);
        try {
            answerList = answerFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (answerList.size() != 4) {
            Log.w(LandingPage.TAG, "There are not exactly 4 answers in the list");
            Toast.makeText(this, "AnswerList is not size of 4!", Toast.LENGTH_SHORT).show();
        }
        Collections.shuffle(answerList);

        updateView();

        // IF DONE WITH LIVEDATA
//        questionRepository.getQuestionByTopicId(topicId).observe(this, (List<Question>questionList) -> {
//            if (questionList == null) {
//                Log.i(LandingPage.TAG, "NOQuestion ???");
//                Toast.makeText(this, "Error loading question. Try restarting app.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            questionsList = questionList;
//            question = questionsList.get(new Random().nextInt(questionsList.size()));
//
//            // Answers
//            answerRepository.getAnswerListByQuestionId(question.answerListId).observe(this, answerList -> {
//                if (answerList == null) {
//                    Toast.makeText(this, "Error loading answers. Try restarting app.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                this.answerList = answerList;
//                if (this.answerList.size() != 4){
//                    Log.w(LandingPage.TAG, "There are not exactly 4 answers in the list");
//                    Toast.makeText(this, "AnswerList is not size of 4!", Toast.LENGTH_SHORT).show();
//                }
//                Collections.shuffle(this.answerList);
//
//                updateView();
//            });
//        });


        // Buttons onClick
        binding.endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResultActivity.class));
            }
        });

        binding.skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        binding.buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(0).isCorrect;
                if (isCorrect) {
                    Toast.makeText(QuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                    nextQuestion();
                    // Add points to player
                } else {
                    Toast.makeText(QuestionActivity.this, "False! Try again", Toast.LENGTH_SHORT).show();
                    // Points that can be gained are reduced
                }
            }
        });

        binding.buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(1).isCorrect;
                if (isCorrect) {
                    Toast.makeText(QuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                    nextQuestion();
                    // Add points to player
                } else {
                    Toast.makeText(QuestionActivity.this, "False! Try again", Toast.LENGTH_SHORT).show();
                    // Points that can be gained are reduced
                }
            }
        });

        binding.buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(2).isCorrect;
                if (isCorrect) {
                    Toast.makeText(QuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                    nextQuestion();
                    // Add points to player
                } else {
                    Toast.makeText(QuestionActivity.this, "False! Try again", Toast.LENGTH_SHORT).show();
                    // Points that can be gained are reduced
                }
            }
        });

        binding.buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCorrect = answerList.get(3).isCorrect;
                if (isCorrect) {
                    Toast.makeText(QuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                    nextQuestion();
                    // Add points to player
                } else {
                    Toast.makeText(QuestionActivity.this, "False! Try again", Toast.LENGTH_SHORT).show();
                    // Points that can be gained are reduced
                }
            }
        });
    }

    private void nextQuestion() {
        if (page < maxpage) {
            if (questionsList.size() <= 1) {
                Toast.makeText(this, "No more questions to ask!", Toast.LENGTH_SHORT).show();
                Intent intent = ChooseTypeActivity.chooseTypeIntentFactory(getApplicationContext(), userId);
                startActivity(intent);
                return;
            }

            questionsList.remove(question);
            question = questionsList.get(new Random().nextInt(questionsList.size()));

            // Answer
            Callable<List<Answer>> answerTask = () -> answerRepository.getAnswerListByQuestionId(question.answerListId);
            Future<List<Answer>> answerFuture = executor.submit(answerTask);
            try {
                answerList = answerFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (answerList.size() != 4) {
                Log.w(LandingPage.TAG, "There are not exactly 4 answers in the list");
                Toast.makeText(this, "AnswerList is not size of 4!", Toast.LENGTH_SHORT).show();
            }
            Collections.shuffle(answerList);

            updateView();

            page += 1;
            String page_str = page + "";
            binding.textViewPage.setText(page_str);
        }
        else {
            Intent intent = ChooseTypeActivity.chooseTypeIntentFactory(getApplicationContext(), userId);
            startActivity(intent);
        }
    }

    private void updateView() {
        binding.textViewQuestion.setText(question.getQuestion());
        binding.buttonAnswer1.setText(answerList.get(0).answer);
        binding.buttonAnswer2.setText(answerList.get(1).answer);
        binding.buttonAnswer3.setText(answerList.get(2).answer);
        binding.buttonAnswer4.setText(answerList.get(3).answer);
    }
}