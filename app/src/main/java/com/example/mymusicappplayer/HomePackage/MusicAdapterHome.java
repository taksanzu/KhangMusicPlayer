package com.example.mymusicappplayer.HomePackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymusicappplayer.MusicPlayerPackage.MusicPlayerActivity;
import com.example.mymusicappplayer.MusicPlayerPackage.MyMediaPlayer;
import com.example.mymusicappplayer.R;

import java.util.ArrayList;

public class MusicAdapterHome extends RecyclerView.Adapter<MusicAdapterHome.ViewHolder> {
    ArrayList<MusicModelHome> songsList;
    Context context;

    public MusicAdapterHome(ArrayList<MusicModelHome> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new MusicAdapterHome.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MusicModelHome musicModelHome = songsList.get(position);
        Glide.with(context).load(songsList.get(position).getSongImage()).into(holder.imageSong);
        holder.songTitle.setText(musicModelHome.getSongTitle());
        holder.singer.setText(musicModelHome.getSongSinger());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("Class","Home");
                intent.putExtra("LISTHOME",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView singer, songTitle;
        ImageView imageSong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.SongName);
            singer = itemView.findViewById(R.id.SingerName);
            imageSong = itemView.findViewById(R.id.icon_view);

        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterSongs(ArrayList<MusicModelHome> filterList){
        songsList = filterList;
        notifyDataSetChanged();
    }
}
