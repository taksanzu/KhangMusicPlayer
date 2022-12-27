package com.example.mymusicappplayer.MainPackage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mymusicappplayer.AlbumPackage.AlbumFragment;
import com.example.mymusicappplayer.FolderPackage.FolderFragment;
import com.example.mymusicappplayer.HomePackage.HomeFragment;
import com.example.mymusicappplayer.SomeStuff.SettingFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new AlbumFragment();
            case 2:
                return new FolderFragment();
            case 3:
                return new SettingFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
