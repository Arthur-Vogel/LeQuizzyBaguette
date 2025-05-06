package com.example.quizz.answer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quizz.AppDatabase;

import java.util.List;

@Dao
public interface AnswerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Answer answer);

    @Delete
    void delete(Answer answer);

    @Query("DELETE FROM " + AppDatabase.ANSWER_TABLE)
    void deleteAll();

    @Query("DELETE FROM " + AppDatabase.ANSWER_TABLE + " WHERE questionId == :questionId")
    void deleteAllByQuestionId(int questionId);

    @Query("SELECT * FROM " + AppDatabase.ANSWER_TABLE + " WHERE questionId == :questionId")
    List<Answer> getAnswerListByQuestionId(int questionId);
}
