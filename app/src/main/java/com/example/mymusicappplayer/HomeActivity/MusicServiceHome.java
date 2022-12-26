package com.example.mymusicappplayer.HomeActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.mymusicappplayer.MusicplayerActivity.ActionPlaying;


public class MusicServiceHome extends Service {
    private IBinder iBinder = new MyBinderHome();
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    ActionPlaying actionPlaying;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    public class MyBinderHome extends Binder {
        public MusicServiceHome getService(){
            return MusicServiceHome.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myActionHome");
        if (actionName != null){
            switch (actionName){
                case ACTION_NEXT:
                    if (actionPlaying != null){
                        actionPlaying.playNextSongHome();
                    }
                    break;
                case ACTION_PLAY:
                    if (actionPlaying != null){
                        actionPlaying.pausePlayHome();
                    }
                    break;
                case ACTION_PREV:
                    if (actionPlaying != null){
                        actionPlaying.playPreviousSongHome();
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
