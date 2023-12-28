package com.example.onelinerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PostJokeActivity extends AppCompatActivity implements NetworkingManager.NetworkingInterfaceListener {

    EditText postEditText;
    Button postBtn,cancelBtn;
    NetworkingManager networkingManager;
    DataBaseManager dataBaseManager;
    JsonManager jsonManager;
    Jokes jokes;
    ImageView heartIcon,shareIcon;
    boolean isNetworkSuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_joke);

        postEditText = findViewById(R.id.post_et);
        postBtn = findViewById(R.id.post_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        heartIcon = findViewById(R.id.heart_icon);
        shareIcon = findViewById(R.id.share_icon);

        networkingManager = ((MyApp)getApplication()).networkingManger;
        dataBaseManager = ((MyApp)getApplication()).dataBaseManager;
        jsonManager = ((MyApp)getApplication()).jsonManager;
        jokes = ((MyApp)getApplication()).jokes;

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            networkingManager.postAJoke(postEditText.getText().toString());
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PostJokeActivity.this, MainActivity.class);
                startActivity(in);

            }
        });

        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String joke = "Never trust atoms; they make up everything.";
                Log.d("send in searchActivity","sendInt "+joke);
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
                Log.d("db","insert joke text ");
                heartIcon.setImageDrawable(ContextCompat.getDrawable(PostJokeActivity.this,R.drawable.red_heart_icon));
                ( (MyApp)getApplication()).dataBaseManager.addJokeInBGThread(new Jokes(jokes.jokeId, postEditText.getText().toString()));
            }
        });

    }


    @Override
    public void networkingFinishWithJsonString(String json) {
        if(isNetworkSuccess){
            Toast.makeText(PostJokeActivity.this,json,Toast.LENGTH_LONG);
        }
        else{
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostJokeActivity.this);
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

    @Override
    public void networkingFinishWithBitMapImage(Bitmap d) {

    }

    @Override
    public void networkingFinishImageWithSuccess(boolean b) {

    }

    @Override
    public void networkingFinishImageWithJsonString(String jsonResponse) {

    }
}