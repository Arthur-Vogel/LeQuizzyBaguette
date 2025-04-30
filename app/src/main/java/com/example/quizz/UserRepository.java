package com.example.quizz;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserRepository {
    private final UserDAO userDAO;
    private static UserRepository instance;

    private UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDAO = db.userDAO();
    }

//    public static UserRepository getRepository(Application application) {
//        if (instance == null) {
//            synchronized (UserRepository.class) {
//                if (instance == null) {
//                    instance = new UserRepository(application);
//                }
//            }
//        }
//        return instance;
//    }

    public static UserRepository getRepository(Application application){
        if (instance != null){
            return instance;
        }
        Future<UserRepository> future = AppDatabase.databaseWriteExecutor.submit(
                new Callable<UserRepository>() {
                    @Override
                    public UserRepository call() throws Exception {
                        return new UserRepository(application);
                    }
                }
        );
        try {
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(LandingPage.TAG, "Problem getting GymLogRepository, thread error");
        }
        return null;
    }

    public static UserRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository(application);
                }
            }
        }
        return instance;
    }

    public void insertUser(User... user){
        AppDatabase.databaseWriteExecutor.execute( ()->
        {
            userDAO.insert(user);
        });
    }

    public void renameUser(String newName, int userId) {
        AppDatabase.databaseWriteExecutor.execute(() ->
        {
            userDAO.renameUser(newName, userId);
        });
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userDAO.getUserSync(username, password);
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<User>> getAllUsers(){
        return userDAO.getAllUsers();
    }
}