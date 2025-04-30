package com.example.quizz;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Question.class}, version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private final static String DATABASE_NAME = "quizz_database";
    public static final String QUESTION_TABLE = "question_table";
    public static final String USER_TABLE = "user_table";
    public abstract UserDAO userDAO();
    public abstract QuestionDAO questionDAO();
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
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.isAdmin = true;
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                testUser1.isAdmin = false;
                dao.insert(testUser1);
            });
        }
    };

//    private static final RoomDatabase.Callback addUsersCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            Executors.newSingleThreadExecutor().execute(() -> {
//                UserDAO dao = INSTANCE.userDAO();
//                dao.deleteAll();
//                User admin = new User("admin", "admin123");
//                admin.isAdmin = true;
//                dao.insert(admin, new User("testuser", "testpass"));
//            });
//        }
//    };
}

