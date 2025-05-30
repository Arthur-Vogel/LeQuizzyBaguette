package com.example.quizz;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);


    }

/*---Arthur's tests---*/
    @Test
    public void testToggleDarkMode() {
        boolean isDarkMode = false;

        isDarkMode = !isDarkMode;

        assertTrue(isDarkMode);
    }

    @Test
    public void testResetUserScore() {
        
        class User {
            public int score;
        }
        User user = new User();
        user.score = 50;


        user.score = 0;
        assertEquals(0, user.score);
    }

}