package com.example.mymusicappplayer.MusicplayerActivity;

import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.ACTION_NEXT;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.ACTION_PLAY;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.ACTION_PREV;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.CHANNEL_ID_1;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.CHANNEL_ID_2;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.CHANNEL_ID_3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.example.mymusicappplayer.AlbumPackage.AlbumModel;
import com.example.mymusicappplayer.AlbumPackage.AlbumService;
import com.example.mymusicappplayer.AlbumPackage.AlbumSongModel;
import com.example.mymusicappplayer.AlbumPackage.NotificationReciverAlbum;
import com.example.mymusicappplayer.FolderPackage.MusicModelFolder;
import com.example.mymusicappplayer.FolderPackage.MusicServiceFolder;
import com.example.mymusicappplayer.FolderPackage.NotificationReciverFolder;
import com.example.mymusicappplayer.HomePackage.MusicModelHome;
import com.example.mymusicappplayer.HomePackage.MusicServiceHome;
import com.example.mymusicappplayer.HomePackage.NotificationReciverHome;
import com.example.mymusicappplayer.R;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {
    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn, repeatBtn, backBtn, backToPlaylist;
    CircleImageView circleImageView;
    ArrayList<MusicModelFolder> songsListFolder;
    MusicModelFolder currentSongFolder;
    MusicServiceFolder musicServiceFolder;
    ArrayList<MusicModelHome> songListHome;
    MusicModelHome currentSongHome;
    MusicServiceHome musicServiceHome;
    ArrayList<AlbumSongModel> songListAlbum;
    AlbumSongModel currentSongAlbum;
    AlbumService albumService;
    MediaSessionCompat mediaSession;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x = 0, y = 0;//y = 0: repeatDisable, y = 1: repeat
    int z = 0;// 1:Folder, 2:Home, 3:Album
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        titleTv = findViewById(R.id.song_title);
        currentTimeTv = findViewById(R.id.current_time);
        totalTimeTv = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        repeatBtn = findViewById(R.id.repeat);
        backBtn = findViewById(R.id.backToHome);
        backToPlaylist = findViewById(R.id.backToPlaylist);
        circleImageView = findViewById(R.id.music_icon_big);
        mediaSession = new MediaSessionCompat(this, "PlayerAudio");
        mediaPlayer.reset();
        titleTv.setSelected(true);
        String text = getIntent().getStringExtra("Class");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        switch (text){
            case "Folder":
                z = 1;
                notificationManager.cancel(0);
                songsListFolder = (ArrayList<MusicModelFolder>) getIntent().getSerializableExtra("LIST");
                setResourcesWithMusicFolder();
                if (mediaPlayer.isPlaying()){
                    startAnimationRotate();
                    showNotificationFolder(R.drawable.ic_baseline_pause_24);
                } else {
                    circleImageView.clearAnimation();
                    showNotificationFolder(R.drawable.ic_baseline_play_arrow_24);
                }
                MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mediaPlayer!=null){
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                            if(mediaPlayer.isPlaying()){
                                pausePlay.setImageResource(R.drawable.ic_baseline_pause_24);
                            }else{
                                pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                            }

                        }
                        new Handler().postDelayed(this,100);
                    }
                });
                Intent intent = new Intent(this, MusicServiceFolder.class);
                bindService(intent,this,BIND_AUTO_CREATE);
                break;
            case "Home":
                z = 2;
                notificationManager.cancel(0);
                songListHome = (ArrayList<MusicModelHome>) getIntent().getSerializableExtra("LISTHOME");
                setResourcesWithMusicHome();
                if (mediaPlayer.isPlaying()){
                    startAnimationRotate();
                    showNotificationHome(R.drawable.ic_baseline_pause_24);
                } else {
                    circleImageView.clearAnimation();
                    showNotificationHome(R.drawable.ic_baseline_play_arrow_24);
                }
                MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mediaPlayer!=null){
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                            if(mediaPlayer.isPlaying()){
                                pausePlay.setImageResource(R.drawable.ic_baseline_pause_24);
                            }else{
                                pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                            }

                        }
                        new Handler().postDelayed(this,100);
                    }
                });
                Intent intent1 = new Intent(this, MusicServiceHome.class);
                bindService(intent1,this,BIND_AUTO_CREATE);
                break;
            case "Album":
                z = 3;
                notificationManager.cancel(0);
                songListAlbum = (ArrayList<AlbumSongModel>) getIntent().getSerializableExtra("ALBUM");
                setResourcesWithMusicAlbum();
                if (mediaPlayer.isPlaying()){
                    startAnimationRotate();
                    showNotificationAlbum(R.drawable.ic_baseline_pause_24);
                } else {
                    circleImageView.clearAnimation();
                    showNotificationAlbum(R.drawable.ic_baseline_play_arrow_24);
                }
                MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mediaPlayer!=null){
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                            if(mediaPlayer.isPlaying()){
                                pausePlay.setImageResource(R.drawable.ic_baseline_pause_24);
                            }else{
                                pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                            }

                        }
                        new Handler().postDelayed(this,100);
                    }
                });
                Intent intent2 = new Intent(this, AlbumService.class);
                bindService(intent2,this,BIND_AUTO_CREATE);
                break;

        }
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                switch (text){
//                    case "Folder":
//                        if (mediaPlayer.isLooping()){
//
//                        }else {
//                            if(MyMediaPlayer.currentIndex== songsListFolder.size()-1)
//                                return;
//                            MyMediaPlayer.currentIndex +=1;
//                            mediaPlayer.reset();
//                            startAnimationRotate();
//                            setResourcesWithMusicFolder();
//                            showNotificationFolder(R.drawable.ic_baseline_pause_24);
//                        }
//                        break;
//                    case "Home":
//                        if (mediaPlayer.isLooping()){
//
//                        }else {
//                            if(MyMediaPlayer.currentIndex== songListHome.size()-1)
//                                return;
//                            MyMediaPlayer.currentIndex +=1;
//                            mediaPlayer.reset();
//                            startAnimationRotate();
//                            setResourcesWithMusicHome();
//                            showNotificationHome(R.drawable.ic_baseline_pause_24);
//                        }
//                        break;
//                    case "Album":
//                        if (mediaPlayer.isLooping()){
//
//                        }else {
//                            if(MyMediaPlayer.currentIndex== songListAlbum.size()-1)
//                                return;
//                            MyMediaPlayer.currentIndex +=1;
//                            mediaPlayer.reset();
//                            startAnimationRotate();
//                            setResourcesWithMusicAlbum();
//                            //showNotificationHome(R.drawable.ic_baseline_pause_24);
//                        }
//                        break;
//                }
//            }
//        });
    }


    private void setResourcesWithMusicAlbum() {
        currentSongAlbum = songListAlbum.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSongAlbum.getSongTitle());
        Glide.with(getApplicationContext()).load(currentSongAlbum.getSongImage()).into(circleImageView);
        pausePlay.setOnClickListener(v-> pausePlayAlbum());
        nextBtn.setOnClickListener(v-> playNextSongAlbum());
        previousBtn.setOnClickListener(v-> playPreviousSongAlbum());
        repeatBtn.setOnClickListener(v->repeatMode());
        backBtn.setOnClickListener(v->backToHome());
        backToPlaylist.setOnClickListener(v->backToHome());
        playMusicAlbum();
    }
    void setResourcesWithMusicFolder(){
        currentSongFolder = songsListFolder.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSongFolder.getTitle());
        totalTimeTv.setText(convertToMMSS(currentSongFolder.getDuration()));
        pausePlay.setOnClickListener(v-> pausePlayFolder());
        nextBtn.setOnClickListener(v-> playNextSongFolder());
        previousBtn.setOnClickListener(v-> playPreviousSongFolder());
        repeatBtn.setOnClickListener(v->repeatMode());
        backBtn.setOnClickListener(v->backToHome());
        backToPlaylist.setOnClickListener(v->backToHome());
        playMusicFolder();
    }
    void setResourcesWithMusicHome(){
        currentSongHome = songListHome.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSongHome.getSongTitle());
        Glide.with(getApplicationContext()).load(currentSongHome.getSongImage()).into(circleImageView);
        pausePlay.setOnClickListener(v-> pausePlayHome());
        nextBtn.setOnClickListener(v-> playNextSongHome());
        previousBtn.setOnClickListener(v-> playPreviousSongHome());
        repeatBtn.setOnClickListener(v->repeatMode());
        backBtn.setOnClickListener(v->backToHome());
        backToPlaylist.setOnClickListener(v->backToHome());
        playMusicHome();
    }
    public  static String Convert(int duration){
        Long milis = Long.parseLong(String.valueOf(duration));
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(milis) % TimeUnit.MINUTES.toSeconds(1) );
    }
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
    private void backToHome() {
        onBackPressed();
    }
    private void repeatMode() {
        if (y == 0){
            repeatBtn.setImageResource(R.drawable.repeat);
            mediaPlayer.setLooping(true);
            y = 1;
        }else if(y == 1) {
            repeatBtn.setImageResource(R.drawable.repeatdisable);
            mediaPlayer.setLooping(false);
            y = 0;
        }
    }
    private void playMusicFolder(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSongFolder.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void playMusicHome(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSongHome.getSongUri());
            mediaPlayer.prepare();
            totalTimeTv.setText(Convert(mediaPlayer.getDuration()));
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void playMusicAlbum() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSongAlbum.getSongUri());
            mediaPlayer.prepare();
            totalTimeTv.setText(Convert(mediaPlayer.getDuration()));
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startAnimationRotate() {
        circleImageView.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.infinite_rotate);
        circleImageView.startAnimation(animation);
    }
    @Override
    public void playNextSongFolder() {
        if(MyMediaPlayer.currentIndex== songsListFolder.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusicFolder();
        showNotificationFolder(R.drawable.ic_baseline_pause_24);
    }
    @Override
    public void playPreviousSongFolder() {
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusicFolder();
        showNotificationFolder(R.drawable.ic_baseline_pause_24);

    }
    @Override
    public void pausePlayFolder() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            circleImageView.clearAnimation();
            showNotificationFolder(R.drawable.ic_baseline_play_arrow_24);
        }
        else{
            mediaPlayer.start();
            startAnimationRotate();
            showNotificationFolder(R.drawable.ic_baseline_pause_24);
        }
    }
    @Override
    public void playNextSongHome() {
        if(MyMediaPlayer.currentIndex== songListHome.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusicHome();
        showNotificationHome(R.drawable.ic_baseline_pause_24);
    }
    @Override
    public void playPreviousSongHome() {
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusicHome();
        showNotificationHome(R.drawable.ic_baseline_pause_24);
    }
    @Override
    public void pausePlayHome() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            circleImageView.clearAnimation();
            showNotificationHome(R.drawable.ic_baseline_play_arrow_24);
        }
        else{
            mediaPlayer.start();
            startAnimationRotate();
            showNotificationHome(R.drawable.ic_baseline_pause_24);
        }
    }
    @Override
    public void playNextSongAlbum() {if(MyMediaPlayer.currentIndex== songListAlbum.size()-1)
        return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusicAlbum();
        showNotificationAlbum(R.drawable.ic_baseline_pause_24);

    }
    @Override
    public void playPreviousSongAlbum() {
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusicAlbum();
        showNotificationAlbum(R.drawable.ic_baseline_pause_24);
    }
    @Override
    public void pausePlayAlbum() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            circleImageView.clearAnimation();
            showNotificationAlbum(R.drawable.ic_baseline_play_arrow_24);
        }
        else{
            mediaPlayer.start();
            startAnimationRotate();
            showNotificationAlbum(R.drawable.ic_baseline_pause_24);
        }
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (z == 1){
            MusicServiceFolder.MyBinder binder =(MusicServiceFolder.MyBinder) iBinder;
            musicServiceFolder = binder.getService();
            musicServiceFolder.sendCallBack(MusicPlayerActivity.this);
        }else if (z == 2){
            MusicServiceHome.MyBinderHome binder =(MusicServiceHome.MyBinderHome) iBinder;
            musicServiceHome = binder.getService();
            musicServiceHome.sendCallBack(MusicPlayerActivity.this);
        }else if (z == 3){
            AlbumService.MyBinderAlbum binderAlbum = (AlbumService.MyBinderAlbum) iBinder;
            albumService = binderAlbum.getService();
            albumService.sendCallBack(MusicPlayerActivity.this);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (z == 1){
            musicServiceFolder = null;
        }else if (z == 2){
            musicServiceHome = null;
        }else if (z == 3){
            albumService = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (z == 1){
            Intent intent = new Intent(this, MusicServiceFolder.class);
            bindService(intent,this,BIND_AUTO_CREATE);
        }else if (z == 2){
            Intent intent = new Intent(this, MusicServiceHome.class);
            bindService(intent,this,BIND_AUTO_CREATE);
        } else if (z == 3){
            Intent intent = new Intent(this, AlbumService.class);
            bindService(intent,this,BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private void showNotificationAlbum(int pausePlay) {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE);
        Intent prevIntent = new Intent(this, NotificationReciverAlbum.class)
                .setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent playIntent = new Intent(this, NotificationReciverAlbum.class)
                .setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent nextIntent = new Intent(this, NotificationReciverAlbum.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent,
                PendingIntent.FLAG_MUTABLE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_3)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(bitmap)
                .setContentTitle(currentSongAlbum.getSongTitle())
                .setContentText(currentSongAlbum.getSongTitle())
                .addAction(R.drawable.ic_baseline_skip_previous_24,"Previous", prevPendingIntent)
                .addAction(pausePlay,"Play", playPendingIntent)
                .addAction(R.drawable.ic_baseline_skip_next_24,"Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
    public void showNotificationFolder(int pausePlay){
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE);
        Intent prevIntent = new Intent(this, NotificationReciverFolder.class)
                .setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent playIntent = new Intent(this, NotificationReciverFolder.class)
                .setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent nextIntent = new Intent(this, NotificationReciverFolder.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent,
                PendingIntent.FLAG_MUTABLE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(bitmap)
                .setContentTitle(currentSongFolder.getTitle())
                .setContentText(currentSongFolder.getTitle())
                .addAction(R.drawable.ic_baseline_skip_previous_24,"Previous", prevPendingIntent)
                .addAction(pausePlay,"Play", playPendingIntent)
                .addAction(R.drawable.ic_baseline_skip_next_24,"Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
    public void showNotificationHome(int pausePlay){
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE);
        Intent prevIntent = new Intent(this, NotificationReciverHome.class)
                .setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent playIntent = new Intent(this, NotificationReciverHome.class)
                .setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent nextIntent = new Intent(this, NotificationReciverHome.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent,
                PendingIntent.FLAG_MUTABLE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(bitmap)
                .setContentTitle(currentSongHome.getSongTitle())
                .setContentText(currentSongHome.getSongTitle())
                .addAction(R.drawable.ic_baseline_skip_previous_24,"Previous", prevPendingIntent)
                .addAction(pausePlay,"Play", playPendingIntent)
                .addAction(R.drawable.ic_baseline_skip_next_24,"Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

}
