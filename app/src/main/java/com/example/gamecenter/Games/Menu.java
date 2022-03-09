package com.example.gamecenter.Games;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.Credentials.Settings;
import com.example.gamecenter.R;
import com.example.gamecenter.Scores.MenuScores;


public class Menu extends AppCompatActivity {
    private static final String TAG = "Menu";
    TextView juego1Button;
    TextView juego2Button;
    TextView scoreView, settingsView;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        username = getIntent().getExtras().getString("usuario");
        Log.d(TAG, "onCreate: " + username);
        juego1Button = (TextView) findViewById(R.id.TextViewPlay1);
        juego1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent juego1 = new Intent(Menu.this, Game2048.class);
                juego1.putExtra("usuario", username);
                startActivity(juego1);

            }
        });

        juego2Button = (TextView) findViewById(R.id.TextViewPlay2);
        juego2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent juego2 = new Intent(Menu.this, GamePeg.class);
                juego2.putExtra("usuario", username);
                startActivity(juego2);
            }
        });

        scoreView = (TextView) findViewById(R.id.TextViewScores);
        scoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scores = new Intent(Menu.this, MenuScores.class);
                scores.putExtra("usuario", username);
                startActivity(scores);

            }
        });

        settingsView = (TextView) findViewById(R.id.TextViewSettings);
        settingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(Menu.this, Settings.class);
                settings.putExtra("usuario", username);
                startActivity(settings);
            }
        });
    }
}
