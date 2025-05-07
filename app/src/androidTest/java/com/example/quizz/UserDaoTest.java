package com.example.quizz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.quizz.question.Question;
import com.example.quizz.question.QuestionDAO;
import com.example.quizz.user.User;
import com.example.quizz.user.UserDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    private AppDatabase db;
    private UserDAO userDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDao = db.userDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertUser_andGetUserSync_returnsCorrectUser() {
        User user = new User("alice","pass123");
        user.id = 42;

        userDao.insert(user);
        User result = userDao.getUserSync("alice", "pass123");

        assertNotNull(result);
        assertEquals("alice", result.username);
        assertEquals("pass123", result.password);
    }

    @Test
    public void renameUser_updatesUsernameCorrectly() {
        User user = new User("bob", "xyz");
        user.id = 2;

        userDao.insert(user);
        userDao.renameUser("robert", 2);

        User updatedUser = userDao.getUserSync("robert", "xyz");

        assertNotNull(updatedUser);
        assertEquals("robert", updatedUser.username);
    }

}

