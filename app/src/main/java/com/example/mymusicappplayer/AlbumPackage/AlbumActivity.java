package com.example.mymusicappplayer.AlbumPackage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;;
import android.widget.Toast;

import com.example.mymusicappplayer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<AlbumSongModel> albumSongList = new ArrayList<>();
    DatabaseReference databaseReference;
    AlbumSongAdapter albumSongAdapter;
    FirebaseStorage storage;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        text = getIntent().getStringExtra("ALBUM");
        recyclerView = findViewById(R.id.rv_MusicListAlbumSong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("/album/"+text+"/albumPath");
        storage = FirebaseStorage.getInstance();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AlbumSongModel albumSongModel = dataSnapshot.getValue(AlbumSongModel.class);
                    albumSongModel.setmKey(snapshot.getKey());
                    Uri uri = Uri.parse(albumSongModel.getSongPath());
                    albumSongModel.setSongUri(uri.toString());
                    albumSongList.add(albumSongModel);
                }
                albumSongAdapter = new AlbumSongAdapter(albumSongList,getApplicationContext());
                recyclerView.setAdapter(albumSongAdapter);
                albumSongAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}