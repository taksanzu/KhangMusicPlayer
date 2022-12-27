package com.example.mymusicappplayer.SomeStuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymusicappplayer.MainPackage.MainActivity;
import com.example.mymusicappplayer.R;

public class SplashScreen extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.splashImage);
        startAnimationRotate();
        loadData();
    }

    private void loadData() {
        if (AppUtil.isNetworkActivity(this)){
            //Network Connected
            //LoadData
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }else {
            Toast.makeText(this, "Yêu cầu mở mạng lên",Toast.LENGTH_LONG).show();
            imageView.clearAnimation();
        }
    }
    private void startAnimationRotate() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_splash);
        imageView.startAnimation(animation);
    }
}