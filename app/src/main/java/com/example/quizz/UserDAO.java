package com.example.quizz;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... users);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    LiveData<User> getUser(String username, String password);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User getUserSync(String username, String password);
}

