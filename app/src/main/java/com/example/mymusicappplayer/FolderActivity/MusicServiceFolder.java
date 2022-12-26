package com.example.mymusicappplayer.FolderActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.mymusicappplayer.MusicplayerActivity.ActionPlaying;


public class MusicServiceFolder extends Service {
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
        public MusicServiceFolder getService(){
            return MusicServiceFolder.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myAction");
        if (actionName != null){
            switch (actionName){
                case ACTION_NEXT:
                    if (actionPlaying != null){
                        actionPlaying.playNextSongFolder();
                    }
                    break;
                case ACTION_PLAY:
                    if (actionPlaying != null){
                        actionPlaying.pausePlayFolder();
                    }
                    break;
                case ACTION_PREV:
                    if (actionPlaying != null){
                        actionPlaying.playPreviousSongFolder();
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
