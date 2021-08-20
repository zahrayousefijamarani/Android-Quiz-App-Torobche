package com.sharifdev.torobche;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.sharifdev.torobche.backUtils.AuthUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
//        Intent i = new Intent(this, Home.class);
//        startActivity(i);
      
        // Show login page
        setContentView(R.layout.login);
        // check if user already logged in, Go to Home
        checkLocalUser();
    }

    private void checkLocalUser() {
        final TextView status = findViewById(R.id.login_status);
        ParseUser currentUser = ParseUser.getCurrentUser();

        // todo: temporary key for logout (to be removed)
        Button logout = findViewById(R.id.logout_tmp);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        status.setVisibility(View.VISIBLE);
                        if (e == null) {
                            status.setTextColor(Color.BLACK);
                            status.setText(R.string.logout);
                        } else {
                            status.setTextColor(Color.RED);
                            status.setText(e.getMessage());
                        }
                    }
                });
            }
        });
        //

        if (currentUser != null) {
            status.setTextColor(Color.GREEN);
            status.setText(String.format("%s%s", getString(R.string.already_logged), currentUser.getUsername()));
            status.setVisibility(View.VISIBLE);

            // Go to Home
            Intent intent = new Intent(this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        setLoginViews();
    }

    private void setLoginViews() {
        Button createAccount = ((Button) findViewById(R.id.create_account_btn));
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpPage = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpPage);
            }
        });
        Button login = findViewById(R.id.login_btn);
        final ProgressBar progressBar = findViewById(R.id.login_prgrsbar);
        final TextView loginStatus = findViewById(R.id.login_status);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ParseUser.logInInBackground(((TextInputEditText) findViewById(R.id.username_inp_login2)).getText().toString()
                        , ((TextInputEditText) findViewById(R.id.password_inp_login2)).getText().toString()
                        , new AuthUtils.UserLoginCallback(loginStatus, progressBar, getApplicationContext()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ParseUser.logOut();
    }
}