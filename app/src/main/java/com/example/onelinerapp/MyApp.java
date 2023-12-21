package com.example.onelinerapp;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApp extends Application {

    JsonManager jsonManager = new JsonManager();
    Jokes jokes = new Jokes();
    ArrayList<Jokes> list = new ArrayList<>();

    NetworkingManager networkingManger = new NetworkingManager();
    static ExecutorService executorService = Executors.newFixedThreadPool(4);
    static Handler mainhandler = new Handler(Looper.getMainLooper());
    DataBaseManager dataBaseManager = new DataBaseManager();

}
