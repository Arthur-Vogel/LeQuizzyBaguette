package com.example.quizz.question;

import android.app.Application;

import com.example.quizz.AppDatabase;

public class QuestionRepository {
    private final QuestionDAO questionDAO;
    private static QuestionRepository instance;

    private QuestionRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        questionDAO = db.questionDAO();
    }


}
