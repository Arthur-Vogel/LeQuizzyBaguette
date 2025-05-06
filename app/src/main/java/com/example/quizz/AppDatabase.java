package com.example.quizz;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quizz.answer.Answer;
import com.example.quizz.answer.AnswerDAO;
import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionDAO;
import com.example.quizz.user.User;
import com.example.quizz.user.UserDAO;
import com.example.quizz.topic.Topic;
import com.example.quizz.topic.TopicDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Question.class, Answer.class, Topic.class}, version = 17, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private final static String DATABASE_NAME = "quizz_database";
    public static final String QUESTION_TABLE = "question_table";
    public static final String ANSWER_TABLE = "answer_table";
    public static final String USER_TABLE = "user_table";
    public static final String TOPIC_TABLE = "topic_table";

    public abstract UserDAO userDAO();
    public abstract QuestionDAO questionDAO();
    public abstract AnswerDAO answerDAO();
    public abstract TopicDAO topicDAO();
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(LandingPage.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                TopicDAO topicDAO = INSTANCE.topicDAO();
                topicDAO.deleteAll();
                topicDAO.insert(new Topic("Food"));
                topicDAO.insert(new Topic("Culture"));
                topicDAO.insert(new Topic("History"));
                topicDAO.insert(new Topic("Geography"));
                topicDAO.insert(new Topic("Famous People"));

                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.isAdmin = true;
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                testUser1.isAdmin = false;
                dao.insert(testUser1);

                // Insert Question & Answer default values
                QuestionDAO questionDAO = INSTANCE.questionDAO();
                AnswerDAO answerDAO = INSTANCE.answerDAO();
                questionDAO.deleteAll();
                Question question1 = new Question("What is the capital of France?", 0, 0, 100);
                questionDAO.insert(question1);
                Answer answer1 = new Answer(question1.id, "Lyon", false);
                answerDAO.insert(answer1);
                Answer answer2 = new Answer(question1.id, "Paris", true);
                answerDAO.insert(answer2);
                Answer answer3 = new Answer(question1.id, "Marseille", false);
                answerDAO.insert(answer3);
                Answer answer4 = new Answer(question1.id, "Toulouse", false);
                answerDAO.insert(answer4);

                Question question2 = new Question("What colors are in the French flag?", 1, 0, 100);
                questionDAO.insert(question2);
                Answer answer5 = new Answer(question2.id, "Blue, White, Red, Black", false);
                answerDAO.insert(answer5);
                Answer answer6 = new Answer(question2.id, "Purple, Blue, White", false);
                answerDAO.insert(answer6);
                Answer answer7 = new Answer(question2.id, "Blue, White, Red", true);
                answerDAO.insert(answer7);
                Answer answer8 = new Answer(question2.id, "Yellow, Orange, Red", false);
                answerDAO.insert(answer8);
            });
        }
    };

}

