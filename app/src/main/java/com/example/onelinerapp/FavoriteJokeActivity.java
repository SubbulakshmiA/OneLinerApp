package com.example.onelinerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FavoriteJokeActivity extends AppCompatActivity implements DataBaseManager.DataBaseManagerInterfaceListener,
                                                            FavoriteRecyclerViewAdapter.FavListClickListener{

    FavoriteRecyclerViewAdapter adapter;
    ArrayList<Jokes> list ;//= new ArrayList<>() ;
    RecyclerView favJokeRecyclerView;
    DataBaseManager dataBaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_joke);

        favJokeRecyclerView = findViewById(R.id.fav_recycler_view);
        dataBaseManager = ((MyApp)getApplication()).dataBaseManager;
        list = ((MyApp)getApplication()).list;
        dataBaseManager.getDb(this);
        dataBaseManager.listener = this;
        adapter = new FavoriteRecyclerViewAdapter(this,list);
        adapter.listener = this;
        dataBaseManager.getAllJokesInBGThread();
        favJokeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favJokeRecyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteJokeActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBaseManager.deleteJokeInBGThread(list.get(pos));
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(favJokeRecyclerView);

    }

    @Override
    public void databaseGetListOfJokes(List<Jokes> listFromDB) {
        list = (ArrayList<Jokes>) listFromDB;
        adapter.list = (ArrayList<Jokes>) listFromDB;
        adapter.notifyDataSetChanged();

    }

    @Override
    public void shareItemClickListener(String joke) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,joke);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}