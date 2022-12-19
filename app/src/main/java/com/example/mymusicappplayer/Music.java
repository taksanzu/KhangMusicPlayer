package com.example.mymusicappplayer;

public class Music {
    String title;
    String image;
    String musicurl;

    public Music(String title, String image, String musicurl) {
        this.title = title;
        this.image = image;
        this.musicurl = musicurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMusicurl() {
        return musicurl;
    }

    public void setMusicurl(String musicurl) {
        this.musicurl = musicurl;
    }
}
