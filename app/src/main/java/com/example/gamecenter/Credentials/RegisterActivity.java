package com.example.gamecenter.Credentials;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.R;
import com.example.gamecenter.Utils.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    private Button signup;
    private Button signin;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editRepassword;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        this.editUsername = findViewById(R.id.username);
        this.editPassword = findViewById(R.id.password);
        this.editRepassword = findViewById(R.id.repassword);
        this.signup = findViewById(R.id.btnsignup);
        this.DB = new DBHelper(this);
        this.signin = findViewById(R.id.btnsignin);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + editUsername + "" + editPassword + "" + editRepassword);
                if (checkEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("All fields must be filled")
                            .setTitle("ERROR");
                    builder.create().show();
                } else if (checkSamePassword()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("The passwords aren't equals")
                            .setTitle("ERROR");
                    builder.create().show();
                } else {
                    boolean CheckFound = DB.insertNewUser(editUsername.getText().toString().toLowerCase(), editPassword.getText().toString());
                    if (CheckFound) {
                        Intent intent = new Intent(RegisterActivity.this, Login.class);
                        startActivity(intent);
                        Toast toast = Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("This user alredy exist.")
                                .setTitle("ERROR");
                        builder.create().show();
                    }
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @return Revisa que sean el mismo valor
     */
    private boolean checkSamePassword() {
        return !editPassword.getText().toString().equals(editRepassword.getText().toString());
    }

    /**
     * @return Revisa que está vacio
     */
    private boolean checkEmpty() {
        return editUsername.getText().toString().isEmpty() ||
                editPassword.getText().toString().isEmpty() ||
                editRepassword.getText().toString().isEmpty();
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
