package com.example.quizz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class QuestionDaoTest {

    private AppDatabase db;
    private QuestionDAO questionDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        questionDao = db.questionDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void testInsertQuestion_insertsCorrectly() {
        Question q = new Question("What's 9 + 10?", 1, 1, 5);
        questionDao.insert(q);

        List<Question> questions = questionDao.getAllQuestion();
        assertEquals(1, questions.size());
        assertEquals("What's 9 + 10?", questions.get(0).getQuestion());
    }

    @Test
    public void testDeleteQuestion_deletesCorrectly() {
        Question q = new Question("Delete me", 2, 1, 10);
        questionDao.insert(q);

        List<Question> allQuestions = questionDao.getAllQuestion();
        assertEquals(1, allQuestions.size());

        questionDao.delete(allQuestions.get(0));
        assertTrue(questionDao.getAllQuestion().isEmpty());
    }
}

