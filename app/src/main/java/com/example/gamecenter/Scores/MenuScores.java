package com.example.gamecenter.Scores;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.Games.Menu;
import com.example.gamecenter.R;

public class MenuScores extends AppCompatActivity {
    String username;
    TextView TextViewPlay1;
    TextView TextViewPlay2;
    TextView Back;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_scores);
        username = getIntent().getExtras().getString("usuario");
        Log.d(TAG, "onCreate: " + username);
        TextViewPlay1 = (TextView) findViewById(R.id.TextViewPlay1);
        TextViewPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuScore2048 = new Intent(MenuScores.this, Game2048Score.class);
                menuScore2048.putExtra("usuario", username);
                startActivity(menuScore2048);
            }
        });
        TextViewPlay2 = (TextView) findViewById(R.id.TextViewPlay2);
        TextViewPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TextViewPlay2 = new Intent(MenuScores.this, GamePegScore.class);
                TextViewPlay2.putExtra("usuario", username);
                startActivity(TextViewPlay2);
            }
        });
        Back = (TextView) findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Back = new Intent(MenuScores.this, Menu.class);
                Back.putExtra("usuario", username);
                startActivity(Back);
            }
        });

    }
}
