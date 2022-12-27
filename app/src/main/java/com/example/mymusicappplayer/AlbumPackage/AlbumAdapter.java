package com.example.mymusicappplayer.AlbumPackage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymusicappplayer.R;

import java.util.ArrayList;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    ArrayList<AlbumModel> albumList;
    Context context;

    public AlbumAdapter(ArrayList<AlbumModel> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_items,parent,false);
        return new AlbumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumModel albumModel = albumList.get(position);
        Glide.with(context).load(albumList.get(position).getAlbumImage()).into(holder.albumImage);
        holder.albumTitle.setText(albumModel.getAlbumTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,albumModel.getMkey(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra("ALBUM",albumModel.getAlbumId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView albumTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumItemImage);
            albumTitle = itemView.findViewById(R.id.albumItemTitle);
        }
    }
}
