package com.example.gamecenter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class daoUsuario extends SQLiteOpenHelper {
    //Constantes
    private static final String TAG = "BBDD";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GameCenter";
    public static final String TABLE_NAME = "datosUsuarios";

    //Columnas
    public static final String col_id = "_Id";
    public static final String col_user = "Username";
    public static final String col_pass = "Password";
    public static final String col_name = "Nombre";
    public static final String col_ape1 = "Apellido1";
    public static final String col_ape2 = "Apellido2";
    public static final String col_2048 = "Puntuacion_2048";
    public static final String col_peg = "Puntuacion_Peg";

    //Lista Usuarios
    ArrayList<Usuario> lista = new ArrayList<>();

    SQLiteDatabase sql;
    String bd = "BDUsusuarios";
    String table = "create table usuario(id integer primary key autoincrement, username text, " +
            "pass text, apellido1 text, apellido2 text, puntuacion_2048 integer, " +
            "puntuacionpeg integer  )";

    //Create Table Usuarios
    private static final String TableUsers_Create =
            // OJO CON PARENTESIS
            "CREATE TABLE " + TABLE_NAME + " (" +
                    col_id + " INTEGER PRIMARY KEY," +
                    col_user + " TEXT, " +
                    col_pass + " TEXT, " +
                    col_name + " TEXT, " +
                    col_ape1 + " TEXT, " +
                    col_ape2 + " TEXT, " +
                    col_2048 + " INTEGER, " +
                    col_peg + " INTEGER " + " );";

    //Drop Table Usuarios
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    //Constructor
    public daoUsuario(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.bd = bd;
    }

    // Variables de instancia para leer y escribir BBDD
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableUsers_Create);
        fullyDB(db);
    }

    //Introducimos datos por defecto
    private void fullyDB(SQLiteDatabase db) {
        lista.add(new Usuario("Uadmin", "Padmin", "Nadmin", "AP1admin",
                "AP2admin", 0, 0));

        //Contenedor para datos
        ContentValues values = new ContentValues();

        // Recorrer lista y almacenamiento datos

        for (int i = 0; i < lista.size(); i++) {
            values.put(col_user, lista.get(i).getUsername());
            values.put(col_pass, lista.get(i).getPassword());
            values.put(col_name, lista.get(i).getNombre());
            values.put(col_ape1, lista.get(i).getApellido1());
            values.put(col_ape2, lista.get(i).getApellido2());
            values.put(String.valueOf(col_2048), lista.get(i).getPuntuacion2048());
            values.put(String.valueOf(col_peg), lista.get(i).getPuntuacionpeg());
            db.insert(TABLE_NAME, null, values);
            //Comprobacion errores por terminal
            Log.d(TAG, " usuarios iniciales: Username" + lista.get(i).getUsername() +
                    " | Password: " + lista.get(i).getPassword() +
                    " | Nombre: " + lista.get(i).getNombre() +
                    " | Apellido1: " + lista.get(i).getApellido1() +
                    " | Apellido2: " + lista.get(i).getApellido2() +
                    " | Puntuacion 2048 " + lista.get(i).getPuntuacion2048() +
                    " | Puntuacion Peg " + lista.get(i).getPuntuacionpeg());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void insertNewUser(String username, String password, String nombre, String apellido1,
                              String apellido2) {
        long newID = 0;
        ContentValues values = new ContentValues();
        values.put(col_user, username);
        values.put(col_pass, password);
        values.put(col_name, nombre);
        values.put(col_ape1, apellido1);
        values.put(col_ape2, apellido2);
        values.put(String.valueOf(col_2048), 0);
        values.put(String.valueOf(col_peg), 0);
        try {
            // Si no hay BBDD, se crea una
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            newID = mWritableDB.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPCION! " + e.getMessage());
        }
    }

    public String searchPass(String col_user, SQLiteDatabase db) {

       /* String[] queryFields = {
                col_user
        };*/
        //String selection = col_user + " = ?";
        String[] selectionArgs = {col_user};
        String user = null;
        String userPass = null;

        try {

            // consulta
            Cursor c = db.rawQuery("Select * From datosUsuarios Where Username = ?" , selectionArgs);
            c.moveToFirst();
            user = c.getString(0);
            userPass = c.getString(1);
        } catch (Exception e) {
            Log.d(TAG, "usuario: no encontrado");
        }
        return user;
    }

    public int Login(String username, String password, SQLiteDatabase db) {
        String[] selectionArgs = new String[]{username, password};
        try {
            int i = 0;
            Cursor c = null;
            c = db.rawQuery("select * from TABLE_NAME where username=? and password=?", selectionArgs);
            c.moveToFirst();
            i = c.getCount();
            c.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteUser(String username, SQLiteDatabase db) {
        String selection = col_user + " LIKE ?";
        String[] selectionArgs = {username};
        db.delete(TABLE_NAME, selection, selectionArgs);
        Log.d(TAG, "usuario borrado " + username);
    }
}
