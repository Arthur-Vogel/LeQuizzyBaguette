package com.example.quizz.question;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quizz.AppDatabase;
import com.example.quizz.User;

import java.util.List;

@Dao
public interface QuestionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question... question);

    @Delete
    void delete(Question question);

    @Query("DELETE FROM " + AppDatabase.QUESTION_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + AppDatabase.QUESTION_TABLE + " ORDER BY id")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.QUESTION_TABLE + " WHERE topicId == :topicId" )
    LiveData<User> getQuestionsByTopicId(int topicId);

    @Query("SELECT question FROM " + AppDatabase.QUESTION_TABLE + " WHERE id == :questionId" )
    String getQuestionById(int questionId);

    @Query("SELECT id FROM " + AppDatabase.QUESTION_TABLE + " WHERE question == :questionString")
    int getIdByQuestionString(String questionString);
}
