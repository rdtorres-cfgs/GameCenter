package com.example.gamecenter.Credentials;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.Games.Menu;
import com.example.gamecenter.R;
import com.example.gamecenter.Utils.DBHelper;

public class Settings extends AppCompatActivity {

    EditText EditUserame, EditPassword, EditRePassword;
    Button EditSave;
    DBHelper helper;
    SQLiteDatabase db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        username = getIntent().getExtras().getString("usuario");
        String usuario = username.toString();

        helper = new DBHelper(this);
        db = helper.getReadableDatabase();

        EditUserame = findViewById(R.id.EditUsername);
        EditUserame.setText(usuario);
        EditUserame.setEnabled(false);

        EditPassword = findViewById(R.id.EditPassword);
        EditRePassword = findViewById(R.id.EditRePassword);
        EditSave = findViewById(R.id.save);

        EditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EditPassword.getText().toString().equals(EditRePassword.getText().toString())) {
                    boolean change = helper.changePassword(EditUserame.getText().toString(),
                            EditPassword.getText().toString());
                    if (change) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                        builder.setMessage("Password succesfully changed")
                                .setTitle("Succesfull");
                        builder.create().show();
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent settingAct = new Intent(Settings.this, Menu.class);
                                settingAct.putExtra("usuario", username);
                                startActivity(settingAct);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                        builder.setMessage("An error has occurred")
                                .setTitle("Error");
                        builder.create().show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setMessage("The passwords aren't equals")
                            .setTitle("Error");
                    builder.create().show();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(me);
    }
}