package com.example.onelinerapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonManager {
    ArrayList<Jokes> list = new ArrayList<>();

    Jokes jsonOfJokes(String json){
        Jokes jokes = new Jokes();
        try{

            JSONObject jsonObject = new JSONObject(json);
            jokes.setId(jsonObject.getInt("id"));
            jokes.setJoke(jsonObject.getString("joke"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jokes;
    }

    ArrayList<Jokes> jsonToListOfJokes(String json){
        try{
//            Jokes jokes = new Jokes();
            Log.d("list in jsonManager","json "+json);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jokeArray = jsonObject.getJSONArray("jokes");
                for(int i=0; i< jokeArray.length();i++){
                    Jokes jokes = new Jokes();
                    jokes.setId(jokeArray.getJSONObject(i).getInt("id"));
                    jokes.setJoke(jokeArray.getJSONObject(i).getString("joke"));
                    list.add(jokes);
                }
//            (jsonObject.getInt("id"),jsonObject.getString("joke"));
                for(int i=0;i<list.size();i++){
                    Log.d("list i jsonManager","list "+list.get(i).joke);

                }
//            JSONArray jsonArray = new JSONArray(json);
//
//            for (int i = 0 ;i < jsonArray.length(); i++){
//                //String c = jsonArray.get(i).toString();
//                //"city, state, country"
//                list.add(new Jokes(jsonArray.get(i).toString()));
//
//            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

}
