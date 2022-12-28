package com.example.mymusicappplayer.AlbumPackage;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.mymusicappplayer.MusicPlayerPackage.ActionPlaying;

public class AlbumService extends Service {
    private IBinder iBinder = new MyBinderAlbum();
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    ActionPlaying actionPlaying;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MyBinderAlbum extends Binder {
        public AlbumService getService(){
            return AlbumService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myActionAlbum");
        if (actionName != null){
            switch (actionName){
                case ACTION_NEXT:
                    if (actionPlaying != null){
                        actionPlaying.playNextSongAlbum();
                    }
                    break;
                case ACTION_PLAY:
                    if (actionPlaying != null){
                        actionPlaying.pausePlayAlbum();
                    }
                    break;
                case ACTION_PREV:
                    if (actionPlaying != null){
                        actionPlaying.playPreviousSongAlbum();
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
