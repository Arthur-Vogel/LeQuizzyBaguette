package com.example.quizz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quizz.topic.Topic;
import com.example.quizz.topic.TopicRepository;

import java.util.List;

public class ChooseTypeViewModel extends AndroidViewModel {

    private final TopicRepository topicRepository;
    public LiveData<List<Topic>> allTopics;

    public ChooseTypeViewModel(@NonNull Application application) {
        super(application);
        topicRepository = TopicRepository.getRepository(application);
        allTopics = topicRepository.getAllTopics();
    }
}
