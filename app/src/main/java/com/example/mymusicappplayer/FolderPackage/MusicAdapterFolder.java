package com.example.mymusicappplayer.FolderPackage;

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

import com.example.mymusicappplayer.MusicPlayerPackage.MusicPlayerActivity;
import com.example.mymusicappplayer.MusicPlayerPackage.MyMediaPlayer;
import com.example.mymusicappplayer.R;

import java.util.ArrayList;

public class MusicAdapterFolder extends RecyclerView.Adapter<MusicAdapterFolder.ViewHolder> {
    ArrayList<MusicModelFolder> songsList;
    Context context;

    public MusicAdapterFolder(ArrayList<MusicModelFolder> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new MusicAdapterFolder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MusicModelFolder songData = songsList.get(position);
        holder.titleTextView.setText(songData.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another acitivty
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("Class","Folder");
                intent.putExtra("LIST",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        ImageView iconImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.SongName);
            iconImageView = itemView.findViewById(R.id.icon_view);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterSongs(ArrayList<MusicModelFolder> filterList){
        songsList = filterList;
        notifyDataSetChanged();
    }
}
