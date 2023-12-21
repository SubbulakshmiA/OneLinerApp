package com.example.onelinerapp;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Jokes {

    @PrimaryKey(autoGenerate = true)
    int jokeId;

    int id;
    String joke;

    public Jokes(String jokeRow) {
        String[] allCityNames =  jokeRow.split(",");
        this.id = Integer.parseInt(allCityNames[0]);
        this.joke = allCityNames[1];
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public Jokes(int id, String joke) {
        this.id = id;
        this.joke = joke;
    }

    public Jokes() {

    }
}
