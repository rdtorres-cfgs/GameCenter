package com.example.gamecenter.Games;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.R;
import com.example.gamecenter.Utils.DBHelper;

import java.util.Random;


public class Game2048 extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static int min_distance = 100;
    String resultRandomNumber;
    private GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private Button[][] matrix = new Button[4][4];
    private boolean gameOver = false;
    private int total;
    //private int totalScore = total;
    private int maxScore;
    private View v;
    Chronometer crono;
    //long Time = 0;
    DBHelper helper;
    SQLiteDatabase db;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game2048);
        username = getIntent().getExtras().getString("usuario");
        gestureDetector = new GestureDetector(this, this);

        crono = (Chronometer) findViewById(R.id.Timer);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();

        helper = new DBHelper(this);
        db = helper.getReadableDatabase();
        maxScore = helper.buscarMaxPuntuacion2048(username, maxScore);
        String StringMaxScore = Integer.toString(maxScore);
        Button maxScoreButton = (Button) findViewById(R.id.maxScore);
        maxScoreButton.setText(StringMaxScore);

        insertarMatrixTablero();
        generarNumRandom();
        repintarValoresEnCasillas();

        Button restart = (Button) findViewById(R.id.restartButton);
        restart.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                recreate();
            }
        });

        Button stop = (Button) findViewById(R.id.stopButton);
        stop.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent game2048 = new Intent(Game2048.this, Menu.class);
                game2048.putExtra("usuario", username);
                startActivity(game2048);
            }
        });
    }

    /**
     * @return Devuelve si no hay m??s movimiento el game over.
     */
    public boolean checkGameOver() {
        if ((revisionMovimientoIzquierda() == false && revisionMovimientoAbajo() == false &&
                revisionMovimientoArriba() == false && revisionMovimientoDerecha() == false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GameOver");
            builder.setMessage("Has perdido");
            if (total > maxScore) {
                helper.modificarPuntuacion2048(username, db, total, crono.getText().toString());
                crono.stop();
            }
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    Game2048.this.finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return false;
    }

    /**
     * Pinta el color de la casilla dependiendo del valor que tiene en ese momento.
     */
    public void repintarValoresEnCasillas() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                // colocamos colores segun valor
                if (matrix[i][j].getText().equals("")) {
                    matrix[i][j].setBackground(getDrawable(R.color.btnColor));
                } else if (matrix[i][j].getText().equals("2")) {
                    matrix[i][j].setBackground(getDrawable(R.color.dos));
                } else if (matrix[i][j].getText().equals("4")) {
                    matrix[i][j].setBackground(getDrawable(R.color.cuatro));
                } else if (matrix[i][j].getText().equals("8")) {
                    matrix[i][j].setBackground(getDrawable(R.color.ocho));
                } else if (matrix[i][j].getText().equals("16")) {
                    matrix[i][j].setBackground(getDrawable(R.color.dieciseis));
                } else if (matrix[i][j].getText().equals("32")) {
                    matrix[i][j].setBackground(getDrawable(R.color.treintaydos));
                } else if (matrix[i][j].getText().equals("64")) {
                    matrix[i][j].setBackground(getDrawable(R.color.sesentaycuatro));
                } else if (matrix[i][j].getText().equals("128")) {
                    matrix[i][j].setBackground(getDrawable(R.color.cientoveintiocho));
                } else if (matrix[i][j].getText().equals("256")) {
                    matrix[i][j].setBackground(getDrawable(R.color.dosciencentayseis));
                } else if (matrix[i][j].getText().equals("512")) {
                    matrix[i][j].setBackground(getDrawable(R.color.quinientosdoce));
                } else if (matrix[i][j].getText().equals("1024")) {
                    matrix[i][j].setBackground(getDrawable(R.color.mil));
                } else if (matrix[i][j].getText().equals("2048")) {
                    matrix[i][j].setBackground(getDrawable(R.color.dosmil));
                } else if (matrix[i][j].getText().equals("4096")) {
                    matrix[i][j].setBackground(getDrawable(R.color.cuatromil));
                }
            }
        }
    }


    /**
     * Genera un numero random de entre 2 y 4.
     */
    public void generarNumRandom() {
        int filas = new Random().nextInt(4);
        int columnas = new Random().nextInt(4);
        int numero = new Random().nextInt(10);
        if (numero < 9) {
            numero = 2;
            resultRandomNumber = Integer.toString(numero);
        } else {
            numero = 4;
            resultRandomNumber = Integer.toString(numero);
        }
        while (!matrix[filas][columnas].getText().equals("")) {
            filas = new Random().nextInt(4);
            columnas = new Random().nextInt(4);
        }
        matrix[filas][columnas].setText(resultRandomNumber);
        repintarValoresEnCasillas();
    }

    /**
     * Guarda los valores de la matriz en los distintos botones
     */
    public void insertarMatrixTablero() {
        matrix[0][0] = findViewById(R.id.boton0);
        matrix[0][1] = findViewById(R.id.boton1);
        matrix[0][2] = findViewById(R.id.boton2);
        matrix[0][3] = findViewById(R.id.boton3);
        matrix[1][0] = findViewById(R.id.boton4);
        matrix[1][1] = findViewById(R.id.boton5);
        matrix[1][2] = findViewById(R.id.boton6);
        matrix[1][3] = findViewById(R.id.boton7);
        matrix[2][0] = findViewById(R.id.boton8);
        matrix[2][1] = findViewById(R.id.boton9);
        matrix[2][2] = findViewById(R.id.boton10);
        matrix[2][3] = findViewById(R.id.boton11);
        matrix[3][0] = findViewById(R.id.boton12);
        matrix[3][1] = findViewById(R.id.boton13);
        matrix[3][2] = findViewById(R.id.boton14);
        matrix[3][3] = findViewById(R.id.boton15);
    }

    /**
     * Realiza el movimiento a la derecha
     */
    public void movimientoDerecha() {
        for (int filas = 0; filas < 4; filas++) {
            for (int movimientocol = 0; movimientocol < 3; movimientocol++) {
                //Movimiento derecha hacia la izquierda columna 4 fila 0
                for (int columnas = 3; columnas > 0; columnas--) {
                    if (matrix[filas][columnas].getText().equals("")) {
                        if (!(matrix[filas][columnas - 1].getText().equals(""))) {
                            String numeroCol = (String) matrix[filas][columnas - 1].getText();
                            matrix[filas][columnas].setText(numeroCol);
                            matrix[filas][columnas - 1].setText("");
                        }
                    }
                }
            }
        }
        repintarValoresEnCasillas();
    }

    /**
     * @return Comprueba que el movimiento a la derecah es posible y devuelve True en su caso.
     */
    //Comprobacion movimiento Derecha
    public boolean revisionMovimientoDerecha() {
        for (int filas = 0; filas < 4; filas++) {
            for (int columnas = 3; columnas > 0; columnas--) {
                if (matrix[filas][columnas].getText().equals("") && !matrix[filas][columnas - 1].getText().equals("")) {
                    return true;
                } else if (matrix[filas][columnas].getText().equals(matrix[filas][columnas - 1].getText())
                        && !(matrix[filas][columnas].getText().equals("")))
                    return true;
            }
        }
        return false;
    }

    /**
     * En caso de que sea posible el movimiento, intentar?? sumar las filas, si no son iguales no lo
     * sumar?? en caso de que s?? lo sea, los sumar?? y dar?? el valor.
     */
    public void sumaMovimientoDerecha() {
        for (int filas = 0; filas < 4; filas++) {
            for (int columnas = 3; columnas > 0; columnas--) {
                if (matrix[filas][columnas].getText().equals(matrix[filas][columnas - 1].getText())) {
                    if (matrix[filas][columnas].getText().equals("") || matrix[filas][columnas - 1].getText().equals("")) {
                    } else {
                        String col1 = (String) matrix[filas][columnas].getText();
                        String col2 = (String) matrix[filas][columnas - 1].getText();

                        int col1int = Integer.parseInt(col1);
                        int col2int = Integer.parseInt(col2);
                        int sumaTotalCol = col1int + col2int;

                        total += sumaTotalCol;

                        String sumTotalCol = Integer.toString(total);

                        Button buton = findViewById(R.id.totalScore);
                        buton.setText(sumTotalCol);

                        matrix[filas][columnas].setText(Integer.toString(sumaTotalCol));
                        matrix[filas][columnas - 1].setText("");

                    }
                }
            }
        }
        repintarValoresEnCasillas();
    }

    /**
     * Realiza el movimiento a la izquierda
     */
    public void movimientoIzquierda() {
        for (int filas = 0; filas < 4; filas++) {
            for (int movimientocol = 0; movimientocol < 3; movimientocol++) {
                //Movimiento derecha hacia la izquierda columna 4 fila 0
                for (int columnas = 0; columnas < 3; columnas++) {
                    if (matrix[filas][columnas].getText().equals("")) {
                        if (!(matrix[filas][columnas + 1].getText().equals(""))) {
                            String numeroCol = (String) matrix[filas][columnas + 1].getText();
                            matrix[filas][columnas].setText(numeroCol);
                            matrix[filas][columnas + 1].setText("");
                        }
                    }
                }
            }
        }
        repintarValoresEnCasillas();
    }

    /**
     * @return Comprueba que el movimiento a la izquierda es posible.
     */
    //Comprobacion movimiento Izquierda
    public boolean revisionMovimientoIzquierda() {
        for (int filas = 0; filas < 4; filas++) {
            for (int columnas = 0; columnas < 3; columnas++) {
                if (matrix[filas][columnas].getText().equals("") && !matrix[filas][columnas + 1].getText().equals("")) {
                    return true;
                } else if (matrix[filas][columnas].getText().equals(matrix[filas][columnas + 1].getText())
                        && !(matrix[filas][columnas].getText().equals("")))
                    return true;
            }
        }
        return false;
    }

    /**
     * Suma los valores a la hora de mover a la izquierda y solamente si son el mismo valor.
     */
    public void sumaMovimientoIzquierda() {
        for (int filas = 0; filas < 4; filas++) {
            for (int columnas = 0; columnas < 3; columnas++) {
                if (matrix[filas][columnas].getText().equals(matrix[filas][columnas + 1].getText())) {
                    if (matrix[filas][columnas].getText().equals("") || matrix[filas][columnas + 1].getText().equals("")) {
                    } else {
                        String col1 = (String) matrix[filas][columnas].getText();
                        String col2 = (String) matrix[filas][columnas + 1].getText();

                        int col1int = Integer.parseInt(col1);
                        int col2int = Integer.parseInt(col2);
                        int sumaTotalCol = col1int + col2int;

                        total += sumaTotalCol;

                        String sumTotalCol = Integer.toString(total);

                        Button buton = findViewById(R.id.totalScore);
                        buton.setText(sumTotalCol);

                        matrix[filas][columnas].setText(Integer.toString(sumaTotalCol));
                        matrix[filas][columnas + 1].setText("");
                    }
                }
            }
            repintarValoresEnCasillas();
        }
    }


    /**
     * Realiza el movimiento hacia arriba
     */
    public void movimientoArriba() {
        for (int columnas = 0; columnas < 4; columnas++)
            for (int movimientofil = 0; movimientofil < 3; movimientofil++) {
                //Movimiento derecha hacia la izquierda columna 4 fila 0
                for (int filas = 0; filas < 3; filas++) {
                    if (matrix[filas][columnas].getText().equals("")) {
                        if (!(matrix[filas + 1][columnas].getText().equals(""))) {
                            String numeroFil = (String) matrix[filas + 1][columnas].getText();
                            matrix[filas][columnas].setText(numeroFil);
                            matrix[filas + 1][columnas].setText("");
                        }
                    }
                }
            }
        repintarValoresEnCasillas();
    }

    /**
     * @return Comprueba que se pueda mover hacia arriba.
     */
    public boolean revisionMovimientoArriba() {
        for (int columnas = 0; columnas < 4; columnas++) {
            for (int filas = 0; filas < 3; filas++) {
                if (matrix[filas][columnas].getText().equals("") && !matrix[filas + 1][columnas].getText().equals("")) {
                    return true;
                } else if (matrix[filas][columnas].getText().equals(matrix[filas + 1][columnas].getText())
                        && !(matrix[filas][columnas].getText().equals("")))
                    return true;
            }
        }
        return false;
    }

    /**
     * Suma movimientos hacia arriba si son iguales
     */
    public void sumaMovimientoArriba() {
        for (int columnas = 0; columnas < 4; columnas++) {
            for (int filas = 0; filas < 3; filas++) {
                if (matrix[filas][columnas].getText().equals(matrix[filas + 1][columnas].getText())) {
                    if (matrix[filas][columnas].getText().equals("") || matrix[filas + 1][columnas].getText().equals("")) {
                    } else {
                        String col1 = (String) matrix[filas][columnas].getText();
                        String col2 = (String) matrix[filas + 1][columnas].getText();

                        int fil1int = Integer.parseInt(col1);
                        int fil2int = Integer.parseInt(col2);
                        int sumaTotalFil = fil1int + fil2int;

                        total += sumaTotalFil;

                        String sumTotalCol = Integer.toString(total);

                        Button buton = findViewById(R.id.totalScore);
                        buton.setText(sumTotalCol);

                        matrix[filas][columnas].setText(Integer.toString(sumaTotalFil));
                        matrix[filas + 1][columnas].setText("");
                    }
                }
            }
        }
        repintarValoresEnCasillas();
    }

    /**
     * Realiza el movimiento hacia abajo
     */
    public void movimientoAbajo() {
        for (int columnas = 0; columnas < 4; columnas++) {
            for (int movimientofil = 0; movimientofil < 3; movimientofil++) {
                //Movimiento derecha hacia la izquierda columna 4 fila 0
                for (int filas = 3; filas > 0; filas--) {
                    if (matrix[filas][columnas].getText().equals("")) {
                        if (!(matrix[filas - 1][columnas].getText().equals(""))) {
                            String numeroFil = (String) matrix[filas - 1][columnas].getText();
                            matrix[filas][columnas].setText(numeroFil);
                            matrix[filas - 1][columnas].setText("");
                        }
                    }
                }
            }
            repintarValoresEnCasillas();
        }
    }

    /**
     * @return Comprueba que se pueda mover hacia abajo
     */
    //Comprobacion movimiento Abajo
    public boolean revisionMovimientoAbajo() {
        for (int columnas = 0; columnas < 4; columnas++) {
            for (int filas = 3; filas > 0; filas--) {
                if (matrix[filas][columnas].getText().equals("") && !matrix[filas - 1][columnas].getText().equals("")) {
                    return true;
                } else if (matrix[filas][columnas].getText().equals(matrix[filas - 1][columnas].getText())
                        && !(matrix[filas][columnas].getText().equals("")))
                    return true;
            }
        }
        return false;
    }

    /**
     * Realiza la suma si las casilla coinciden en el movimiento hacia abajo.
     */
    public void sumaMovimientoAbajo() {
        for (int columnas = 0; columnas < 4; columnas++) {
            for (int filas = 3; filas > 0; filas--) {
                if (matrix[filas][columnas].getText().equals(matrix[filas - 1][columnas].getText())) {
                    if (matrix[filas][columnas].getText().equals("") || matrix[filas - 1][columnas].getText().equals("")) {
                    } else {
                        String col1 = (String) matrix[filas][columnas].getText();
                        String col2 = (String) matrix[filas - 1][columnas].getText();

                        int fil1int = Integer.parseInt(col1);
                        int fil2int = Integer.parseInt(col2);
                        int sumaTotalFil = fil1int + fil2int;

                        total += sumaTotalFil;

                        String sumTotalCol = Integer.toString(total);

                        Button buton = findViewById(R.id.totalScore);
                        buton.setText(sumTotalCol);

                        matrix[filas][columnas].setText(Integer.toString(sumaTotalFil));
                        matrix[filas - 1][columnas].setText("");
                    }
                }
            }
            repintarValoresEnCasillas();
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = e.getX();
                y1 = e.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = e.getX();
                y2 = e.getY();

                float valueX = x2 - x1;
                float valueY = y2 - y1;

                if (Math.abs(valueX) > min_distance) {
                    if (x2 > x1) {
                        if (revisionMovimientoDerecha()) {
                            movimientoDerecha();
                            sumaMovimientoDerecha();
                            movimientoDerecha();
                            generarNumRandom();
                            checkGameOver();
                        }
                    } else {
                        if (revisionMovimientoIzquierda()) {
                            movimientoIzquierda();
                            sumaMovimientoIzquierda();
                            movimientoIzquierda();
                            generarNumRandom();
                            checkGameOver();
                        }
                    }
                } else if (Math.abs(valueY) > min_distance) {
                    if (y2 > y1) {
                        if (revisionMovimientoAbajo()) {
                            movimientoAbajo();
                            sumaMovimientoAbajo();
                            movimientoAbajo();
                            generarNumRandom();
                            checkGameOver();
                        }
                    } else {
                        if (revisionMovimientoArriba()) {
                            movimientoArriba();
                            sumaMovimientoArriba();
                            movimientoArriba();
                            generarNumRandom();
                            checkGameOver();
                        }
                    }
                }
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}