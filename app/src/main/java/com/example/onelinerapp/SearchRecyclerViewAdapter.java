package com.example.onelinerapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchRecyclerViewAdapter extends  RecyclerView.Adapter<SearchRecyclerViewAdapter.FavoriteViewHolder>{

    interface FavListClickListener{
        void favItemClickListener(int adapterPosition);
        void shareItemClickListener(int adapterPosition);
    }

    Context context;
    ArrayList<Jokes> list;
    FavListClickListener listener;
    public SearchRecyclerViewAdapter(Context context, ArrayList<Jokes> list) {
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
        View v = LayoutInflater.from(context).inflate(R.layout.search_joke_row_item,parent,false);
        return new FavoriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        TextView favJokeText = holder.itemView.findViewById(R.id.fav_joke_tv);
        ImageView heartIcon = holder.itemView.findViewById(R.id.heart_icon);
        ImageView shareIcon = holder.itemView.findViewById(R.id.share_icon);

        favJokeText.setText(list.get(position).joke);

        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            heartIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.red_heart_icon));
            listener.favItemClickListener(holder.getAdapterPosition());
            }
        });
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.shareItemClickListener(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
