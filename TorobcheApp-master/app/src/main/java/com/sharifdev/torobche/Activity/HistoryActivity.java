package com.sharifdev.torobche.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sharifdev.torobche.R;

import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        int history_number =  Objects.requireNonNull(getIntent().getExtras()).getInt("history_number");

    }
}