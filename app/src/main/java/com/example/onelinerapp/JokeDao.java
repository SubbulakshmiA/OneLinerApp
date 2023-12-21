package com.example.onelinerapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JokeDao {

    @Insert
    void addNewJoke(Jokes jokes);

    @Query("select * from jokes")
    List<Jokes> getAllJokes();

    @Delete
    void deleteAJoke(Jokes deleteJoke);

    @Query("delete from jokes")
    void deleteJokeTable();
}
