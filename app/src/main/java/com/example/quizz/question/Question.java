package com.example.quizz.question;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.quizz.AppDatabase;

import java.util.Objects;

@Entity(tableName = AppDatabase.QUESTION_TABLE)
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String question;

    // Refers to AnswerID from Answer table
    @NonNull
    public int answerId;

    public int topicId;

    public int scorePoints;

    public Question(@NonNull String question, @NonNull int answerId, int topicId,int scorePoints){
        this.question = question;
        this.answerId = answerId;
        this.topicId = topicId;
        this.scorePoints = scorePoints;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getQuestion() {
        return question;
    }

    public void setQuestion(@NonNull String question) {
        this.question = question;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public void setScorePoints(int scorePoints) {
        this.scorePoints = scorePoints;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return id == question1.id && answerId == question1.answerId && topicId == question1.topicId && Objects.equals(question, question1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, answerId, topicId);
    }
}
