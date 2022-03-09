package com.example.gamecenter.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

public class DBHelper extends SQLiteOpenHelper {
    //Constantes
    private static final String TAG = "BBDD";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GameCenter";
    public static final String TABLE_NAME = "user";
    public static final String col_username = "username";
    public static final String col_password = "password";
    public static final String col_score_2048 = "score_2048";
    public static final String col_time_2048 = "time_2048";
    public static final String col_score_peg = "score_peg";
    public static final String col_time_peg = "time_peg";

    //Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Variables de instancia para leer y escribir BBDD
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user (username TEXT PRIMARY KEY, password TEXT NOT NULL, " +
                "score_2048 INTEGER,time_2048 TEXT,score_peg INTEGER, time_peg TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public int buscarMaxPuntuacion2048(String user, int score) {
        boolean found = false;
        int aux = 0;
        String query = ("SELECT " + col_score_2048 + " FROM " + TABLE_NAME +
                " WHERE " + col_username + " = ? ");
        if (this.mReadableDB == null) {
            this.mReadableDB = this.getReadableDatabase();
        }
        Cursor cursor = mReadableDB.rawQuery(query, new String[]{user});

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                aux = cursor.getInt(0);
                return aux;
            }
        }
        Log.d(TAG, "buscarMaxPuntuacion2048: " + aux);
        cursor.close();
        return aux;
    }

    public void modificarPuntuacion2048(String usuario, SQLiteDatabase db, int score_2048, String time_2048) {
        Log.d(TAG, "entramos modificar puntuacio del usuario " + usuario + " | puntuacion nueva es: " + score_2048 + " | tiempo nuevo es: " + time_2048);
        ContentValues values = new ContentValues();
        values.put(col_score_2048, score_2048);
        values.put(col_time_2048, time_2048);
        String selection = col_username + " LIKE ?";
        String[] selectionArgs = {usuario};

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        Log.d(TAG, "nuevo valor " + score_2048 + time_2048 + " filas afectadas: " + count);
    }

    public int buscarMaxPuntuacionPeg(String user, int score) {
        boolean found = false;
        int aux = 0;
        String query = ("SELECT " + col_score_peg + " FROM " + TABLE_NAME +
                " WHERE " + col_username + " = ? ");
        if (this.mReadableDB == null) {
            this.mReadableDB = this.getReadableDatabase();
        }
        Cursor cursor = mReadableDB.rawQuery(query, new String[]{user});

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                aux = cursor.getInt(0);
                return aux;
            }
        }
        Log.d(TAG, "buscarMaxPuntuacion2048: " + aux);
        cursor.close();
        return aux;
    }

    public void modificarPuntuacionPeg(String usuario, SQLiteDatabase db, int score_peg, String time_peg) {
        Log.d(TAG, "entramos modificar puntuacio del usuario " + usuario + " | puntuacion nueva es: " + score_peg + " | tiempo nuevo es: " + time_peg);
        ContentValues values = new ContentValues();
        values.put(col_score_peg, score_peg);
        values.put(col_time_peg, time_peg);
        String selection = col_username + " LIKE ?";
        String[] selectionArgs = {usuario};

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        Log.d(TAG, "nuevo valor " + score_peg + time_peg + " filas afectadas: " + count);
    }

    public boolean insertNewUser(String newUser, String newPass) {
        boolean inserted = false;
        if (this.mWritableDB == null) {
            this.mWritableDB = this.getWritableDatabase();
        }

        try {
            ContentValues values = new ContentValues();
            values.put(col_username, newUser);
            values.put(col_password, newPass);
            mWritableDB.insertOrThrow("user", null, values);
            inserted = true;
            mWritableDB.close();
        } catch (Exception ex) {
            Log.d("Error", ex.getMessage());
            inserted = false;
        }

        return inserted;
    }

    public boolean changePassword(String username, String password) {
        boolean updated = false;
        if (this.mWritableDB == null) {
            this.mWritableDB = this.getWritableDatabase();
        }
        try {
            ContentValues c = new ContentValues();
            c.put(col_password, password);
            mWritableDB.update("user", c, "username = ?", new String[]{username});
            updated = true;
        } catch (Exception ex) {
            Log.d("Error", ex.getMessage());
            updated = false;
        }
        return updated;
    }

    public boolean searchUser(String name, String password) {
        boolean found = false;
        if (this.mReadableDB == null) {
            this.mReadableDB = this.getReadableDatabase();
        }
        Cursor cursor = mReadableDB.rawQuery("SELECT username, password FROM user WHERE username = ? " +
                "and password = ?", new String[]{name, password});

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                if (name.equals(cursor.getString(0))) {
                    found = true;
                }
            }
        }
        cursor.close();

        return found;
    }

    public void mostrarPuntuacion2048(TextView textView, SQLiteDatabase db, String orderBy) {
        Log.d(TAG, "entramos buscarPuntuacion2048 sort");
        String[] projection = {
                col_username,
                col_score_2048,
                col_time_2048
        };
        String user;
        int puntuacion = -1;
        String tiempo;
        String sortOrder = orderBy + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            if (c != null && c.moveToFirst()) {
                String linea = "\n";
                c.moveToFirst();
                user = c.getString(0);
                puntuacion = c.getInt(1);
                tiempo = c.getString(2);
                linea = linea + "----------------------------------------" + "\n" +
                        "USUARIO                  " + user + "\n" +
                        "PUNTUACION:         " + puntuacion + "\n" +
                        "TIEMPO:                   " + tiempo + "\n";
                while (c.moveToNext()) {

                    user = c.getString(0);
                    puntuacion = c.getInt(1);
                    tiempo = c.getString(2);
                    linea = linea + "----------------------------------------" + "\n" +
                            "USUARIO                  " + user + "\n" +
                            "PUNTUACION:         " + puntuacion + "\n" +
                            "TIEMPO:                   " + tiempo + "\n";
                    Log.d(TAG, "cursor  usuario: " + user + " |  puntuacion 2048: " + puntuacion + "|  TIEMPO 2048: " + tiempo);
                }
                textView.setText(linea);
                c.close();
            }
            try {
                c.close();
            } catch (Exception e) {
                Log.d(TAG, " Error al cerrar el cursor | " + e.getMessage());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al buscar puntuacion de todos  los usuarios con order " + e.getMessage());
        }
        Log.d(TAG, "Salir de buscar puntuacion con todos los usuarios ordenados");
    }

    public void mostrarPuntuacion2048Usuario(TextView textView, SQLiteDatabase db, String orderBy) {
        Log.d(TAG, "entramos buscarPuntuacion2048 sort");
        String[] projection = {
                col_username,
                col_score_2048,
                col_time_2048
        };
        String user;
        int puntuacion = -1;
        String tiempo;
        String sortOrder = orderBy + " ASC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            if (c != null && c.moveToFirst()) {
                String linea = "\n";
                c.moveToFirst();
                user = c.getString(0);
                puntuacion = c.getInt(1);
                tiempo = c.getString(2);
                linea = linea + "----------------------------------------" + "\n" +
                        "USUARIO                  " + user + "\n" +
                        "PUNTUACION:         " + puntuacion + "\n" +
                        "TIEMPO:                   " + tiempo + "\n";

                while (c.moveToNext()) {

                    user = c.getString(0);
                    puntuacion = c.getInt(1);
                    tiempo = c.getString(2);
                    linea = linea + "----------------------------------------" + "\n" +
                            "USUARIO                  " + user + "\n" +
                            "PUNTUACION:         " + puntuacion + "\n" +
                            "TIEMPO:                   " + tiempo + "\n";
                    Log.d(TAG, "cursor  usuario: " + user + " |  puntuacion 2048: " + puntuacion + "|  TIEMPO 2048: " + tiempo);
                }
                textView.setText(linea);
                c.close();
            }
            try {
                c.close();
            } catch (Exception e) {
                Log.d(TAG, " Error al cerrar el cursor | " + e.getMessage());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al buscar puntuacion de todos  los usuarios con order " + e.getMessage());
        }
        Log.d(TAG, "Salir de buscar puntuacion con todos los usuarios ordenados");
    }

    public void mostrarPuntuacionPeg(TextView textView, SQLiteDatabase db, String orderBy) {
        Log.d(TAG, "entramos buscarPuntuacion2048 sort");
        String[] projection = {
                col_username,
                col_score_peg,
                col_time_peg
        };
        String user;
        int puntuacion = -1;
        String tiempo;
        String sortOrder = orderBy + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            if (c != null && c.moveToFirst()) {
                String linea = "\n";
                c.moveToFirst();
                user = c.getString(0);
                puntuacion = c.getInt(1);
                tiempo = c.getString(2);
                linea = linea + "----------------------------------------" + "\n" +
                        "USUARIO                  " + user + "\n" +
                        "PUNTUACION:         " + puntuacion + "\n" +
                        "TIEMPO:                   " + tiempo + "\n";
                while (c.moveToNext()) {

                    user = c.getString(0);
                    puntuacion = c.getInt(1);
                    tiempo = c.getString(2);
                    linea = linea + "----------------------------------------" + "\n" +
                            "USUARIO                  " + user + "\n" +
                            "PUNTUACION:         " + puntuacion + "\n" +
                            "TIEMPO:                   " + tiempo + "\n";
                    Log.d(TAG, "cursor  usuario: " + user + " |  puntuacion 2048: " + puntuacion + "|  TIEMPO 2048: " + tiempo);
                }
                textView.setText(linea);
                c.close();
            }
            try {
                c.close();
            } catch (Exception e) {
                Log.d(TAG, " Error al cerrar el cursor | " + e.getMessage());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al buscar puntuacion de todos  los usuarios con order " + e.getMessage());
        }
        Log.d(TAG, "Salir de buscar puntuacion con todos los usuarios ordenados");
    }

    public void mostrarPuntuacionPegUsuario(TextView textView, SQLiteDatabase db, String orderBy) {
        Log.d(TAG, "entramos buscarPuntuacion2048 sort");
        String[] projection = {
                col_username,
                col_score_peg,
                col_time_peg
        };
        String user;
        int puntuacion = -1;
        String tiempo;
        String sortOrder = orderBy + " ASC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );
            if (c != null && c.moveToFirst()) {
                String linea = "\n";
                c.moveToFirst();
                user = c.getString(0);
                puntuacion = c.getInt(1);
                tiempo = c.getString(2);
                linea = linea + "----------------------------------------" + "\n" +
                        "USUARIO                  " + user + "\n" +
                        "PUNTUACION:         " + puntuacion + "\n" +
                        "TIEMPO:                   " + tiempo + "\n";
                while (c.moveToNext()) {

                    user = c.getString(0);
                    puntuacion = c.getInt(1);
                    tiempo = c.getString(2);
                    linea = linea + "----------------------------------------" + "\n" +
                            "USUARIO                  " + user + "\n" +
                            "PUNTUACION:         " + puntuacion + "\n" +
                            "TIEMPO:                   " + tiempo + "\n";
                    Log.d(TAG, "cursor  usuario: " + user + " |  puntuacion 2048: " + puntuacion + "|  TIEMPO 2048: " + tiempo);
                }
                textView.setText(linea);
                c.close();
            }
            try {
                c.close();
            } catch (Exception e) {
                Log.d(TAG, " Error al cerrar el cursor | " + e.getMessage());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error al buscar puntuacion de todos  los usuarios con order " + e.getMessage());
        }
        Log.d(TAG, "Salir de buscar puntuacion con todos los usuarios ordenados");
    }
}
