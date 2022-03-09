package com.example.gamecenter.Games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.gamecenter.Credentials.RegisterActivity;
import com.example.gamecenter.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 2000);
    }
}