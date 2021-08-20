package com.sharifdev.torobche.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sharifdev.torobche.R;

public class ChooseOpponentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_opponent);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }
}