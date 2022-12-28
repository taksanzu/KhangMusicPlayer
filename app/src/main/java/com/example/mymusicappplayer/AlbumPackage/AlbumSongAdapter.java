package com.example.mymusicappplayer.AlbumPackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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


public class AlbumSongAdapter extends RecyclerView.Adapter<AlbumSongAdapter.ViewHolder>{
    ArrayList<AlbumSongModel> albumSongList;
    Context context;

    public AlbumSongAdapter(ArrayList<AlbumSongModel> albumSongList, Context context) {
        this.albumSongList = albumSongList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumSongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new AlbumSongAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AlbumSongAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AlbumSongModel albumSongModel = albumSongList.get(position);
        Glide.with(context).load(albumSongList.get(position).getSongImage()).into(holder.imageSong);
        holder.albumSongTitle.setText(albumSongModel.getSongTitle());
        holder.singer.setText(albumSongModel.getSongSinger());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("Class","Album");
                intent.putExtra("ALBUM",albumSongList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {return albumSongList.size();}
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView singer, albumSongTitle;
        ImageView imageSong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumSongTitle = itemView.findViewById(R.id.SongName);
            singer = itemView.findViewById(R.id.SingerName);
            imageSong = itemView.findViewById(R.id.icon_view);
        }
    }
}
