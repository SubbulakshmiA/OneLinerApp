package com.example.onelinerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkingManager.NetworkingInterfaceListener {

    NetworkingManager networkingManager;
    DataBaseManager dataBaseManager;
    JsonManager jsonManager;
    Jokes jokes;
    Button next;
    ImageView heartIcon,shareIcon;
    ArrayList<Jokes> list;
    boolean isNetworkSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkingManager = ((MyApp)getApplication()).networkingManger;
        dataBaseManager = ((MyApp)getApplication()).dataBaseManager;
        jsonManager = ((MyApp)getApplication()).jsonManager;
        jokes = ((MyApp)getApplication()).jokes;
        dataBaseManager.getDb(this);
        list =  ((MyApp)getApplication()).list;

        networkingManager.getRandomJokes();
        networkingManager.listener = this;

        next = findViewById(R.id.next);
        heartIcon = findViewById(R.id.heart_icon);
        shareIcon = findViewById(R.id.share_icon);

        fragment(jokes.getJoke());
//        dataBaseManager.deleteJokeTableInBGThread();
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String joke = ((MyApp)getApplication()).jokes.joke ;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,joke);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });
        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heartIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.red_heart_icon));
                ( (MyApp)getApplication()).dataBaseManager.addJokeInBGThread(new Jokes(((MyApp)getApplication()).jokes.id,
                                        ((MyApp)getApplication()).jokes.joke));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkingManager.getRandomJokes();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.searchbar_menu_item){
            Intent in = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(in);
        } else if (id == R.id.favorite) {
            Intent in = new Intent(MainActivity.this, FavoriteJokeActivity.class);
            startActivity(in);
        } else if (id == R.id.post) {
            Intent in = new Intent(MainActivity.this, PostJokeActivity.class);
            startActivity(in);
        }
        return true;
    }

    @Override
    public void networkingFinishWithJsonString(String json) {
        Log.d("networkingFinishWithJsonString","networkingFinishWithJsonStrin "+json);
        if(isNetworkSuccess){
            ((MyApp)getApplication()).jokes = jsonManager.jsonOfJokes(json);
            fragment(jokes.joke);
        }else{
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Something went wrong!");
                    builder.setMessage(json);
                    builder.setPositiveButton("Ok",null);
                    builder.create().show();
                }
            });
        }
    }

    @Override
    public void networkingFinishWithSuccess(boolean isSuccess) {
     isNetworkSuccess = isSuccess;
    }
    void fragment(String jokeText) {
        JokeFragment jokeFrag = (JokeFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        JokeFragment jokeFragment =JokeFragment.newInstance(jokeText,"text" );

        if(jokeFrag == null){
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, jokeFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, jokeFragment).commit();
        }
    }

}