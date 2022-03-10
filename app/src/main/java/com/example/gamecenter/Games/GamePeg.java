package com.example.gamecenter.Games;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.R;
import com.example.gamecenter.Utils.DBHelper;

public class GamePeg extends AppCompatActivity {
    public ImageView[][] matrixImageView;
    int[][] BOARD;
    boolean firstClick = true;
    int iV_selectedI;
    int iV_selectedJ;
    int puntuacion = 0;
    private int maxScore;
    Chronometer crono;
    DBHelper helper;
    SQLiteDatabase db;
    String username;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepeg);
        username = getIntent().getExtras().getString("usuario");
        setTitle("Peg Solitaire");
        Game();
        crono = (Chronometer) findViewById(R.id.Timer);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
        helper = new DBHelper(this);
        db = helper.getReadableDatabase();
        maxScore = helper.buscarMaxPuntuacionPeg(username, maxScore);
        Button maxScoreButton = (Button) findViewById(R.id.maxScore);

        Button stop = (Button) findViewById(R.id.stopButton);
        stop.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent game2048 = new Intent(GamePeg.this, Menu.class);
                game2048.putExtra("usuario", username);
                startActivity(game2048);
            }
        });


    }

    public void Game() {
        matrix();
        detectarCasillaClicada();
    }

    public int contarFichasRellenas() {
        int numeroCasillasRellenas = 0;
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                if (matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())) {
                    numeroCasillasRellenas++;
                }
            }
        }
        return numeroCasillasRellenas;

    }

    public boolean checkGameOver() {
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length - 2; j++) {
                //Izquierda-Derecha (Derecha)
                if (matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i][j + 1].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i][j + 2].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_vacia, null).getConstantState())) {
                    return false;
                }//Izquierda-Derecha (Izquierda)
                if (matrixImageView[i][j + 1].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i][j + 2].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_vacia, null).getConstantState())) {
                    return false;
                }
            }
        }
        for (int i = 0; i < matrixImageView.length - 2; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                //Arriba-abajo (Abajo)
                if (matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i + 1][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i + 2][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_vacia, null).getConstantState())) {
                    return false;
                }//Arriba-abajo (Arriba)
                if (matrixImageView[i + 1][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i + 2][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                        && matrixImageView[i][j].getBackground().getConstantState().equals(
                        getResources().getDrawable(R.drawable.ficha_shape_vacia, null).getConstantState())) {
                    return false;
                }
            }
        }
        return true;
    }


    public void matrix() {
        matrixImageView = new ImageView[][]{
                {findViewById(R.id.cell00), findViewById(R.id.cell01), findViewById(R.id.cell02), findViewById(R.id.cell03), findViewById(R.id.cell04), findViewById(R.id.cell05), findViewById(R.id.cell06)},
                {findViewById(R.id.cell10), findViewById(R.id.cell11), findViewById(R.id.cell12), findViewById(R.id.cell13), findViewById(R.id.cell14), findViewById(R.id.cell15), findViewById(R.id.cell16)},
                {findViewById(R.id.cell20), findViewById(R.id.cell21), findViewById(R.id.cell22), findViewById(R.id.cell23), findViewById(R.id.cell24), findViewById(R.id.cell25), findViewById(R.id.cell26)},
                {findViewById(R.id.cell30), findViewById(R.id.cell31), findViewById(R.id.cell32), findViewById(R.id.cell33), findViewById(R.id.cell34), findViewById(R.id.cell35), findViewById(R.id.cell36)},
                {findViewById(R.id.cell40), findViewById(R.id.cell41), findViewById(R.id.cell42), findViewById(R.id.cell43), findViewById(R.id.cell44), findViewById(R.id.cell45), findViewById(R.id.cell46)},
                {findViewById(R.id.cell50), findViewById(R.id.cell51), findViewById(R.id.cell52), findViewById(R.id.cell53), findViewById(R.id.cell54), findViewById(R.id.cell55), findViewById(R.id.cell56)},
                {findViewById(R.id.cell60), findViewById(R.id.cell61), findViewById(R.id.cell62), findViewById(R.id.cell63), findViewById(R.id.cell64), findViewById(R.id.cell65), findViewById(R.id.cell66)},
        };
    }


    public void detectarCasillaClicada() {
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                int finalI = i;
                int finalJ = j;
                matrixImageView[finalI][finalJ].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())
                                || firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficha_shape_clicked, null).getConstantState())) {
                            if (matrixImageView[iV_selectedI][iV_selectedJ].getBackground().getConstantState().equals(
                                    getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())) {
                                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_llena);
                            }
                            matrixImageView[finalI][finalJ].setBackgroundResource(R.drawable.ficha_shape_clicked);
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            firstClick = false;
                        } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficha_shape_vacia, null).getConstantState())) {
                            realizarMovimiento(finalJ, finalI);
                            puntuacion += 5;
                            String Score = Integer.toString(puntuacion);
                            TextView textViewScore = findViewById(R.id.Puntuacion);
                            textViewScore.setText(Score);
                            if (checkGameOver()) {
                                if (contarFichasRellenas() == 1) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePeg.this);
                                    builder.setTitle("Win");
                                    builder.setMessage("Has Ganado");
                                    if (puntuacion > maxScore) {
                                        helper.modificarPuntuacionPeg(username, db,puntuacion, crono.getText().toString());
                                        crono.stop();
                                    }
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                                            GamePeg.this.finish();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(GamePeg.this);
                                    builder.setTitle("Loose");
                                    builder.setMessage("Has Perdido");
                                    if (puntuacion > maxScore) {
                                        helper.modificarPuntuacionPeg(username, db, puntuacion, crono.getText().toString());
                                        crono.stop();
                                    }
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                                            GamePeg.this.finish();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        } else if (!firstClick && matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.ficha_shape_llena, null).getConstantState())) {
                            matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_llena);
                            matrixImageView[finalI][finalJ].setBackgroundResource(R.drawable.ficha_shape_clicked);
                            iV_selectedI = finalI;
                            iV_selectedJ = finalJ;
                            //bucarMovimientoPosibles();
                            firstClick = false;
                        }
                    }
                });
            }
        }
    }

    public void borrarListener() {
        for (int i = 0; i < matrixImageView.length; i++) {
            for (int j = 0; j < matrixImageView[0].length; j++) {
                matrixImageView[i][j].setOnClickListener(null);
                {
                }
            }
        }

    }

    public void realizarMovimiento(int finalJ, int finalI) {
        // Abajo-Arriba
        if (iV_selectedJ == finalJ) {
            if (finalI - iV_selectedI == -2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_vacia);
                // Bola Medio
                matrixImageView[iV_selectedI - 1][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI - 2][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_llena);
                firstClick = true;
            }
            //Arriba - Abajo
            else if (finalI - iV_selectedI == 2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_vacia);
                // Bola Medio
                matrixImageView[iV_selectedI + 1][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI + 2][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_llena);
                firstClick = true;
            }
        } else if (iV_selectedI == finalI) {
            //Izquierda- Derecha
            if (finalJ - iV_selectedJ == 2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_vacia);
                // Bola Medio
                matrixImageView[iV_selectedI][iV_selectedJ + 1].setBackgroundResource(R.drawable.ficha_shape_vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI][iV_selectedJ + 2].setBackgroundResource(R.drawable.ficha_shape_llena);
                firstClick = true;

            }
            //Derecha-Izquierda
            else if (finalJ - iV_selectedJ == -2) {
                //Primera Bola
                matrixImageView[iV_selectedI][iV_selectedJ].setBackgroundResource(R.drawable.ficha_shape_vacia);
                // Bola Medio
                matrixImageView[iV_selectedI][iV_selectedJ - 1].setBackgroundResource(R.drawable.ficha_shape_vacia);
                //Ultima Bola
                matrixImageView[iV_selectedI][iV_selectedJ - 2].setBackgroundResource(R.drawable.ficha_shape_llena);
                firstClick = true;
            }
        } else {
            if (matrixImageView[finalI][finalJ].getBackground().getConstantState().equals(
                    getResources().getDrawable(R.drawable.ficha_shape_clicked, null).getConstantState()) &&
                    matrixImageView[iV_selectedI][iV_selectedJ].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.ficha_shape_vacia, null).getConstantState())) {
            }
        }
    }
}
