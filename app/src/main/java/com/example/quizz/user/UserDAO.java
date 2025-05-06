package com.example.quizz.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quizz.AppDatabase;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM " + AppDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE username = :username AND password = :password")
    LiveData<User> getUser(String username, String password);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE username = :username AND password = :password LIMIT 1")
    User getUserSync(String username, String password);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE username == :username" )
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE id == :userId" )
    LiveData<User> getUserByUserId(int userId);

    @Query("UPDATE " + AppDatabase.USER_TABLE + " SET username = :newName WHERE id == :userId ")
    void renameUser(String newName, int userId);
}

