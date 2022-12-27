package com.example.mymusicappplayer.AlbumPackage;

import java.io.Serializable;

public class AlbumModel implements Serializable {
    public String mkey;
    public String albumTitle;
    public String albumImage;
    public String albumId;

    public AlbumModel(String albumTitle, String albumImage, String albumId) {
        this.albumTitle = albumTitle;
        this.albumImage = albumImage;
        this.albumId = albumId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public AlbumModel() {
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }


    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }
}
