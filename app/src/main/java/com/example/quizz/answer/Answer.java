package com.example.quizz.answer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.quizz.AppDatabase;
import java.util.Objects;

@Entity(tableName = AppDatabase.ANSWER_TABLE)
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // Group of answers
    public int questionId;

    public String answer;
    public boolean isCorrect = false;

    public Answer(int questionId, String answer, boolean isCorrect) {
        this.questionId = questionId;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer1 = (Answer) o;
        return id == answer1.id && questionId == answer1.questionId && isCorrect == answer1.isCorrect && Objects.equals(answer, answer1.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionId, answer, isCorrect);
    }
}
