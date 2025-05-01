package com.example.quizz.question;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.quizz.AppDatabase;
import com.example.quizz.answer.Answer;
import com.example.quizz.answer.AnswerRepository;

import java.util.List;
import java.util.Objects;

@Entity(tableName = AppDatabase.QUESTION_TABLE)
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String question;

    // Refers to AnswerId object from Answer table
    public int answerListId;

    public int topicId;

    public int scorePoints;

    public Question(@NonNull String question, int answerListId, int topicId,int scorePoints){
        this.question = question;
        this.answerListId = answerListId;
        this.topicId = topicId;
        this.scorePoints = scorePoints;

        //answerList = getAnswerListByQuestionId(id);   -> Je crois que ce n'est pas possible de faire ça ici
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
        return id == question1.id && topicId == question1.topicId && Objects.equals(question, question1.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, topicId);
    }
}
