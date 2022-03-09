package com.example.gamecenter.Scores;


import static android.content.ContentValues.TAG;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.R;
import com.example.gamecenter.Utils.DBHelper;

public class Game2048Score extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String username;
    SQLiteDatabase db;
    DBHelper helper;
    TextView textViewTodasPuntuacion;
    Spinner mSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_game2048);
        username = getIntent().getExtras().getString("usuario");
        Log.d(TAG, "onCreate: " + username);

        helper = new DBHelper(this);
        db = helper.getReadableDatabase();
        textViewTodasPuntuacion = findViewById(R.id.scores_players);


        mSpinner = findViewById(R.id.scores_spinner);
        String[] opciones = {"Usuario", "Puntuacion", "Tiempo"};
        mSpinner.setPrompt("Ordenar");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("spinner", "casos");
        String orderBy;
        switch (i) {
            case -1:
                Log.d(TAG, "caso -1 : error");
                break;
            case 0:
                orderBy = "username";
                Log.d(TAG, "caso 0 segun id");
                helper.mostrarPuntuacion2048Usuario(textViewTodasPuntuacion, db, orderBy);
                break;
            case 1:
                orderBy = "score_2048";
                Log.d(TAG, "caso 1 segun score");
                helper.mostrarPuntuacion2048(textViewTodasPuntuacion, db, orderBy);
                break;
            case 2:
                orderBy = "time_2048";
                Log.d(TAG, "caso 2 segun tiempo");
                helper.mostrarPuntuacion2048(textViewTodasPuntuacion, db, orderBy);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}