package com.example.mymusicappplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicPlayerActivity extends AppCompatActivity {
    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn, repeatBtn, backBtn, backToPlaylist;
    CircleImageView circleImageView;
    ArrayList<MusicModel> songsList;
    MusicModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x = 0, y = 0;//y = 0: repeatDisable, y = 1: repeat
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

        titleTv.setSelected(true);

        songsList = (ArrayList<MusicModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();
        if (mediaPlayer.isPlaying()){
            startAnimationRotate();
        } else {
            circleImageView.clearAnimation();
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
                    startAnimationRotate();
                    setResourcesWithMusic();
                }
            }
        });
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

    private void playNextSong(){
        if(MyMediaPlayer.currentIndex== songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        startAnimationRotate();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            circleImageView.clearAnimation();
        }
        else{
            mediaPlayer.start();
            startAnimationRotate();
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
}
