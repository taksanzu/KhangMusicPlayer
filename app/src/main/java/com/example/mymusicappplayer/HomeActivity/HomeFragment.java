package com.example.mymusicappplayer.HomeActivity;



import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mymusicappplayer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<MusicModelHome> songsList = new ArrayList<>();
    DatabaseReference databaseReference;
    MusicAdapterHome musicAdapterHome;
    FirebaseStorage storage;
    Toolbar toolbar;
    MenuItem menuItem;
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = view.findViewById(R.id.toolbarHome);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Trang chủ");
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_MusicListHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        storage = FirebaseStorage.getInstance();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    MusicModelHome musicModelHome = snapshot1.getValue(MusicModelHome.class);
                    musicModelHome.setmKey(snapshot.getKey());
                    Uri uri =  Uri.parse( musicModelHome.getSongPath());
                    musicModelHome.setSongUri(uri.toString());
                    songsList.add(musicModelHome);
                }
                musicAdapterHome = new MusicAdapterHome(songsList,getActivity().getApplicationContext());
                recyclerView.setAdapter(musicAdapterHome);
                musicAdapterHome.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_view,menu);
        MenuItem menuItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchSong(searchView);
    }

    private void searchSong(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterSongs(s.toLowerCase());
                return true;
            }
        });
    }

    private void filterSongs(String s) {
        ArrayList<MusicModelHome> filterList = new ArrayList<>();
        if (songsList.size() > 0){
            for (MusicModelHome musicModelHome : songsList){
                if (musicModelHome.getSongTitle().toLowerCase().contains(s)){
                    filterList.add(musicModelHome);
                }
            }
            if (musicAdapterHome != null){
                musicAdapterHome.filterSongs(filterList);
            }
        }
    }


}