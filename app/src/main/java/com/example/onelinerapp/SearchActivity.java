package com.example.onelinerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements NetworkingManager.NetworkingInterfaceListener,
                                                        SearchRecyclerViewAdapter.FavListClickListener{

    RecyclerView searchRecyclerView;
    ArrayList<Jokes> list = new ArrayList<>();
    SearchRecyclerViewAdapter adapter;
    NetworkingManager networkingManager;
    JsonManager jsonManager;
    Jokes jokes;
    boolean isNetworkSuccess = false;
    String que;
    boolean isNetworkImageSuccess = false;
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<Bitmap> bitmapList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchRecyclerView = findViewById(R.id.search_recycler_view);
        networkingManager = ((MyApp)getApplication()).networkingManger;
        jsonManager = ((MyApp)getApplication()).jsonManager;
        jokes = ((MyApp)getApplication()).jokes;

        networkingManager.listener = this;

        adapter = new SearchRecyclerViewAdapter(this,list,bitmapList);
//        adapter = new SearchRecyclerViewAdapter(this,bitmapList);

        adapter.listener = this;
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        menu.findItem(R.id.searchbar_menu_item).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SearchView searchViewMenu = (SearchView) menu.findItem(R.id.searchbar_menu_item).getActionView();
        searchViewMenu.setQueryHint("Search for Joke ");
        searchViewMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2){
                    que = query;
                    networkingManager.getSearchJokes(query);
                    networkingManager.getRandomImageForJoke(que);

                }
                return false;
            }
        });
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.searchbar_menu_item){
            item.setVisible(false);
        } else if (id == R.id.favorite) {
            Intent in = new Intent(SearchActivity.this, FavoriteJokeActivity.class);
            startActivity(in);
        } else if (id == R.id.post) {
            Intent in = new Intent(SearchActivity.this, PostJokeActivity.class);
            startActivity(in);
        } else if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }
    @Override
    public void networkingFinishWithJsonString(String json) {
        if(isNetworkSuccess){
            list = jsonManager.jsonToListOfJokes(json);
            adapter.list = list;
            adapter.notifyDataSetChanged();
        }else{
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
        isNetworkSuccess = true;

    }

    @Override
    public void networkingFinishWithBitMapImage(Bitmap d) {
    }

    @Override
    public void networkingFinishImageWithSuccess(boolean b) {
        isNetworkImageSuccess = b;
    }

    @Override
    public void networkingFinishImageWithJsonString(String jsonResponse) {
        if(isNetworkSuccess){
            urlList = jsonManager.jsonListOfImageOfJokes(jsonResponse);

            networkingManager.downloadImageList(urlList);

        }
    }

    @Override
    public void networkingFinishWithBitMapImageList(ArrayList<Bitmap> bitmapList) {
//        bitmapList = bitmapList;
        adapter.bitmapList = bitmapList;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void favItemClickListener(int adapterPosition) {
        int id = list.get(adapterPosition).id;
        String joke = list.get(adapterPosition).joke;
        ( (MyApp)getApplication()).dataBaseManager.addJokeInBGThread(new Jokes(id,joke));
    }

    @Override
    public void shareItemClickListener(int adapterPosition) {
        String joke = list.get(adapterPosition).joke;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,joke);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}