package com.example.mymusicappplayer.FolderPackage;

import android.database.Cursor;
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

import java.io.File;
import java.util.ArrayList;


public class FolderFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<MusicModelFolder> songsList = new ArrayList<>();
    MusicAdapterFolder musicAdapterFolder;
    Toolbar toolbar;
    public FolderFragment() {
    }


    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
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
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        toolbar = view.findViewById(R.id.toolbarFolder);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Thư mục");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_view,menu);
        MenuItem menuItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchSong(searchView);
    }

    private void searchSong(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText.toLowerCase());
                return true;
            }
        });
    }

    private void filterSongs(String toLowerCase) {
        ArrayList<MusicModelFolder> filterList = new ArrayList<>();
        if (songsList.size() > 0){
            for (MusicModelFolder musicModelFolder : songsList){
                if (musicModelFolder.getTitle().toLowerCase().contains(toLowerCase)){
                    filterList.add(musicModelFolder);
                }
            }
            if ( musicAdapterFolder != null){
                musicAdapterFolder.filterSongs(filterList);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_MusicListFolder);
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while(cursor.moveToNext()){
            MusicModelFolder songData = new MusicModelFolder(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if(new File(songData.getPath()).exists())
                songsList.add(songData);
        }

        if(songsList.size()==0){
           Toast.makeText(getActivity().getApplicationContext(),"Không có bài nào hiển thị", Toast.LENGTH_SHORT).show();
        }else{
            //recyclerview
            musicAdapterFolder = new MusicAdapterFolder(songsList,getActivity().getApplicationContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.setAdapter(musicAdapterFolder);
            musicAdapterFolder.notifyDataSetChanged();
        }
    }

}