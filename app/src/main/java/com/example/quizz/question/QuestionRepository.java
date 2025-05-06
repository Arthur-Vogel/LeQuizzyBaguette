package com.example.quizz.question;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.quizz.AppDatabase;
import com.example.quizz.LandingPage;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class QuestionRepository {
    private final QuestionDAO questionDAO;
    private static QuestionRepository instance;

    private QuestionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        questionDAO = db.questionDAO();
    }

    public static QuestionRepository getRepository(Application application) {
        if (instance != null) {
            return instance;
        }
        Future<QuestionRepository> future = AppDatabase.databaseWriteExecutor.submit(
                new Callable<QuestionRepository>() {
                    @Override
                    public QuestionRepository call() throws Exception {
                        return new QuestionRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(LandingPage.TAG, "Problem getting QuestionRepository, thread error");
        }
        return null;
    }

    public static QuestionRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (QuestionRepository.class) {
                if (instance == null) {
                    instance = new QuestionRepository(application);
                }
            }
        }
        return instance;
    }

    public void insertQuestion(Question... question) {
        AppDatabase.databaseWriteExecutor.execute( ()->
        {
            questionDAO.insert(question);
        });
    }

    public void deleteQuestion(Question question) {
        AppDatabase.databaseWriteExecutor.execute( () ->
        {
            questionDAO.delete(question);
        });
    }

    public LiveData<Question> getQuestionByTopicId(int topicId) {
        return questionDAO.getQuestionByTopicId(topicId);
    }

    public LiveData<List<Question>> getAllQuestion() {
        return questionDAO.getAllQuestions();
    }

    public void deleteAllByTopicId(int topicId) {
        AppDatabase.databaseWriteExecutor.execute( () ->
        {
            questionDAO.deleteAllByTopicId(topicId);
        });
    }

    public String getQuestionById(int questionId) {
        return questionDAO.getQuestionById(questionId);
    }

    public int getIdByQuestionString(String questionString) {
        return questionDAO.getIdByQuestionString(questionString);
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(questionDAO::deleteAll);
    }
}
