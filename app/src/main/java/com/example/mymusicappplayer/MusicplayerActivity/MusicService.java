package com.example.mymusicappplayer.MusicplayerActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class MusicService extends Service {
    private IBinder iBinder = new MyBinder();
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    ActionPlaying actionPlaying;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    public class MyBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myAction");
        if (actionName != null){
            switch (actionName){
                case ACTION_NEXT:
                    if (actionPlaying != null){
                        actionPlaying.playNextSong();
                    }
                    break;
                case ACTION_PLAY:
                    if (actionPlaying != null){
                        actionPlaying.pausePlay();
                    }
                    break;
                case ACTION_PREV:
                    if (actionPlaying != null){
                        actionPlaying.playPreviousSong();
                    }
                    break;
            }
        }
        return START_STICKY;
    }
    public void sendCallBack(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }
}
