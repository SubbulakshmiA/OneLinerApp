package com.example.onelinerapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Jokes.class},version = 1)
public abstract class JokeDatabase extends RoomDatabase {
        public abstract JokeDao getDao();


}
