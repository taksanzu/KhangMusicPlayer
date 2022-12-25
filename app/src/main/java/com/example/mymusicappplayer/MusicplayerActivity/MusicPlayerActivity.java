package com.example.mymusicappplayer.MusicplayerActivity;

import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.ACTION_NEXT;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.ACTION_PLAY;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.ACTION_PREV;
import static com.example.mymusicappplayer.MusicplayerActivity.ApplicationClass.CHANNEL_ID_2;

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
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.mymusicappplayer.FolderActivity.MusicModel;
import com.example.mymusicappplayer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {
    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn, repeatBtn, backBtn, backToPlaylist;
    CircleImageView circleImageView;
    ArrayList<MusicModel> songsList;
    MusicModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x = 0, y = 0;//y = 0: repeatDisable, y = 1: repeat
    MusicService musicService;
    boolean isPlaying = false;
    MediaSessionCompat mediaSession;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
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
        titleTv.setSelected(true);
        songsList = (ArrayList<MusicModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();
        if (mediaPlayer.isPlaying()){
            startAnimationRotate();
            isPlaying = true;
            Log.e( "Playing",isPlaying + "" );
            showNotification(R.drawable.ic_baseline_pause_24);
        } else {
            circleImageView.clearAnimation();
            isPlaying = false;
            showNotification(R.drawable.ic_baseline_play_arrow_24);
        }
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_24);
                        showNotification(R.drawable.ic_baseline_pause_24);
                    }else{
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        showNotification(R.drawable.ic_baseline_play_arrow_24);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer.isLooping()){

                }else {
                    if(MyMediaPlayer.currentIndex== songsList.size()-1)
                        return;
                    MyMediaPlayer.currentIndex +=1;
                    mediaPlayer.reset();
                    isPlaying = true;
                    Log.e( "Playing",isPlaying + "" );
                    startAnimationRotate();
                    setResourcesWithMusic();
                    showNotification(R.drawable.ic_baseline_pause_24);
                }
            }
        });
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
    }
    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTv.setText(currentSong.getTitle());

        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());
        repeatBtn.setOnClickListener(v->repeatMode());
        backBtn.setOnClickListener(v->backToHome());
        backToPlaylist.setOnClickListener(v->backToHome());
        playMusic();
    }

    private void backToHome() {
        finish();
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
    private void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
    private void startAnimationRotate() {
        circleImageView.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.infinite_rotate);
        circleImageView.startAnimation(animation);
    }

    @Override
    public void playNextSong() {
        if(MyMediaPlayer.currentIndex== songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        isPlaying = true;
        Log.e( "Playing",isPlaying + "" );
        startAnimationRotate();
        setResourcesWithMusic();
        showNotification(R.drawable.ic_baseline_pause_24);
    }

    @Override
    public void playPreviousSong() {
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        isPlaying = true;
        Log.e( "Playing",isPlaying + "" );
        startAnimationRotate();
        setResourcesWithMusic();
        showNotification(R.drawable.ic_baseline_pause_24);
    }

    @Override
    public void pausePlay() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            circleImageView.clearAnimation();
            isPlaying = false;
            Log.e( "Playing",isPlaying + "" );
            showNotification(R.drawable.ic_baseline_play_arrow_24);
        }
        else{
            mediaPlayer.start();
            startAnimationRotate();
            isPlaying = true;
            Log.e( "Playing",isPlaying + "" );
            showNotification(R.drawable.ic_baseline_pause_24);

        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder =(MusicService.MyBinder) iBinder;
        musicService = binder.getService();
        musicService.sendCallBack(MusicPlayerActivity.this);
        Log.e("Connected",musicService + "" );
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
        Log.e("Disconected",musicService + "" );

    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
    }

    public void showNotification(int pausePlay){
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE);
        Intent prevIntent = new Intent(this, NotificationReciver.class)
                .setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent playIntent = new Intent(this, NotificationReciver.class)
                .setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent,
                PendingIntent.FLAG_MUTABLE);
        Intent nextIntent = new Intent(this, NotificationReciver.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent,
                PendingIntent.FLAG_MUTABLE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(bitmap)
                .setContentTitle(currentSong.getTitle())
                .setContentText(currentSong.getTitle())
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
