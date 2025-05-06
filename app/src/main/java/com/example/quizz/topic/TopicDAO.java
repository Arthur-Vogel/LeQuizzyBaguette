package com.example.quizz.topic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quizz.AppDatabase;
import java.util.List;

@Dao
public interface TopicDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Topic... topic);

    @Delete
    void delete(Topic topic);

    @Query("DELETE FROM " + AppDatabase.TOPIC_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + AppDatabase.TOPIC_TABLE + " ORDER BY id")
    LiveData<List<Topic>> getAllTopics();
}
