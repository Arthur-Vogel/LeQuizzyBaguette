package com.example.quizz.answer;

import android.app.Application;
import android.util.Log;

import com.example.quizz.AppDatabase;
import com.example.quizz.LandingPage;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AnswerRepository {
    private final AnswerDAO answerDAO;
    private static AnswerRepository instance;

    private AnswerRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        answerDAO = db.answerDAO();
    }

    public static AnswerRepository getRepository(Application application) {
        if (instance != null) {
            return instance;
        }
        Future<AnswerRepository> future = AppDatabase.databaseWriteExecutor.submit(
                new Callable<AnswerRepository>() {
                    @Override
                    public AnswerRepository call() throws Exception {
                        return new AnswerRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(LandingPage.TAG, "Problem getting AnswerRepository, thread error");
        }
        return null;
    }

    public List<Answer> getAnswerListByQuestionId(int questionId) {
        return answerDAO.getAnswerListByQuestionId(questionId);
    }
}
