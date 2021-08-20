package com.sharifdev.torobche;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseUser;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.AuthUtils;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setSignUpViews();
    }

    private void setSignUpViews() {
        final TextInputEditText email = findViewById(R.id.su_email_inp);
        final TextInputEditText username = findViewById(R.id.su_username_inp);
        final TextInputEditText password = findViewById(R.id.su_password_inp);
        final TextView result = findViewById(R.id.result);
        final TextView passStrength = findViewById(R.id.password_strength);
        Button btn = findViewById(R.id.sign_up_btn);
        final ProgressBar progressBar = findViewById(R.id.singup_progress);
        setPasswordChange(passStrength, password);
        setSignUpBtn(result, username, email, password, passStrength, progressBar, btn);
    }

    private void setPasswordChange(final TextView passStrength, TextInputEditText password) {
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passStrength.setText(R.string.not_set);
                passStrength.setTextColor(Color.RED);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    passStrength.setText(R.string.not_set);
                    passStrength.setTextColor(Color.RED);
                } else if (s.length() < 6) {
                    passStrength.setText(R.string.weak);
                    passStrength.setTextColor(Color.RED);
                } else if (s.length() < 10) {
                    passStrength.setText(R.string.medium);
                    passStrength.setTextColor(getResources().getColor(R.color.green_light));
                } else if (s.length() < 20) {
                    passStrength.setText(R.string.strong);
                    passStrength.setTextColor(getResources().getColor(R.color.green));
                } else {
                    passStrength.setTextColor(Color.RED);
                    passStrength.setText(R.string.max_reach);
                }
            }
        });
    }

    private void setSignUpBtn(final TextView result, final TextInputEditText username, final TextInputEditText email
            , final TextInputEditText password, final TextView passStrength, final ProgressBar progressBar
            , Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (passStrength.getText().equals(getString(R.string.weak)) ||
                        passStrength.getText().equals(getString(R.string.not_entered)) ||
                        passStrength.getText().equals(getString(R.string.max_reach))
                ) {
                    progressBar.setVisibility(View.GONE);
                    result.setText(R.string.incorrect_input);
                    return;
                }

                AuthUtils.signUpUser(username.getText().toString(), password.getText().toString(),
                        email.getText().toString(),
                        new AuthUtils.UserSignUpCallback(result, progressBar, getSupportFragmentManager()
                                , ((TextInputEditText) findViewById(R.id.entered_code))
                                , ((TextView) findViewById(R.id.code_error)), getApplicationContext()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ParseUser.logOut();
    }
}
