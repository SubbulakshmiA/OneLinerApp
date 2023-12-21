package com.example.onelinerapp;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

public class DataBaseManager {
    interface DataBaseManagerInterfaceListener{
        void databaseGetListOfJokes(List<Jokes> l);
    }

    JokeDatabase db ;
    DataBaseManagerInterfaceListener listener;

    JokeDatabase getDb(Context context){
        if (db == null){
            db = Room.databaseBuilder(context,JokeDatabase.class,"joke-db").build();
        }
        return db;
    }
    void addJokeInBGThread(Jokes joke){
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                db.getDao().addNewJoke(joke);
            }
        });
    }
    void getAllJokesInBGThread(){
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Jokes> list =  db.getDao().getAllJokes();
                MyApp.mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // main thread
                        listener.databaseGetListOfJokes(list);
                    }
                });

            }
        });
    }

    void deleteJokeInBGThread(Jokes deleteJoke){
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                db.getDao().deleteAJoke(deleteJoke);
            }
        });
    }
    void deleteJokeTableInBGThread(){
        MyApp.executorService.execute(new Runnable() {
            @Override
            public void run() {
                db.getDao().deleteJokeTable();
            }
        });
    }


}
