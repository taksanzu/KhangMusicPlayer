package com.example.mymusicappplayer.HomeActivity;


import java.io.Serializable;

public class MusicModelHome implements Serializable {
    public String mKey;
    public String songImage;
    public String songPath;
    public String songTitle;
    public String songSinger;
    public String songUri;

    public MusicModelHome(String songImage, String songPath, String songTitle, String songSinger,String songUri) {
        this.songImage = songImage;
        this.songPath = songPath;
        this.songTitle = songTitle;
        this.songSinger = songSinger;
        this.songUri = songUri;
    }

    public MusicModelHome() {
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getSongImage() {
        return songImage;
    }

    public void setSongImage(String songImage) {
        this.songImage = songImage;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongSinger() {
        return songSinger;
    }

    public void setSongSinger(String songSinger) {
        this.songSinger = songSinger;
    }

    public String getSongUri() {
        return songUri;
    }

    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }
}
