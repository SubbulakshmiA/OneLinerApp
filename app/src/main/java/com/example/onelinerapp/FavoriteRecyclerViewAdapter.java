package com.example.onelinerapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteRecyclerViewAdapter extends  RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteViewHolder>{

    interface FavListClickListener{
        void shareItemClickListener(String j);
    }

    Context context;
    ArrayList<Jokes> list;
    FavListClickListener listener;
    public FavoriteRecyclerViewAdapter(Context context, ArrayList<Jokes> list) {
        this.context = context;
        this.list = list;
    }
    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fav_joke_row_item,parent,false);
        return new FavoriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        TextView favJokeText = holder.itemView.findViewById(R.id.fav_joke_tv);
        favJokeText.setText(list.get(position).joke);

        ImageView shareIcon = holder.itemView.findViewById(R.id.share_icon);
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.shareItemClickListener(list.get(holder.getAdapterPosition()).joke);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
