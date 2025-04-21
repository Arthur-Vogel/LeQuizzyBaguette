package com.example.quizz;

import android.app.Application;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private final UserDAO userDAO;
    private static UserRepository instance;

    private UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDAO = db.userDAO();
    }

    public static UserRepository getRepository(Application application) {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository(application);
                }
            }
        }
        return instance;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userDAO.getUserSync(username, password);
    }
}