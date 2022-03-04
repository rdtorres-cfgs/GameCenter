package com.example.gamecenter;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        //Instancia de BBDD
        daoUsuario dao = new daoUsuario(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SQLiteDatabase db = dao.getReadableDatabase();

                String userName = username.getText().toString();
                String passPut = password.getText().toString();
                String passBBDD = dao.searchPass(userName,db);

                Log.d(TAG, "onClick: "+ userName+" "+passPut+" "+
                        passBBDD+ "");
                if(passPut.equals(passBBDD)){
                    Intent intent = new Intent(Login.this, Menu.class);
                    intent.putExtra("Username",userName);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: "+ userName+" "+passPut+" "+
                            passBBDD+ "");
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }
}
