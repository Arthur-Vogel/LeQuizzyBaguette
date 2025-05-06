package com.example.quizz.topic;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;

import com.example.quizz.AppDatabase;
import com.example.quizz.LandingPage;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TopicRepository {
    private final TopicDAO topicDAO;
    private static TopicRepository instance;

    private TopicRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        topicDAO = db.topicDAO();
    }

    public static TopicRepository getRepository(Application application) {
        if (instance != null) {
            return instance;
        }
        Future<TopicRepository> future = AppDatabase.databaseWriteExecutor.submit(
                new Callable<TopicRepository>() {
                    @Override
                    public TopicRepository call() throws Exception {
                        return new TopicRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(LandingPage.TAG, "Problem getting TopicRepository, thread error");
        }
        return null;
    }

    public LiveData<List<Topic>> getAllTopics() {
        return topicDAO.getAllTopics();
    }

    public int getTopicIdFromName(String topicName){return topicDAO.getTopicIdFromName(topicName);}

    public Topic getTopicFromName(String topicName){
        return topicDAO.getTopicFromName(topicName);
    }
}
