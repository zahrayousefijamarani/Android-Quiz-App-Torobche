package com.sharifdev.torobche.Category;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharifdev.torobche.R;

import java.util.Objects;

public class CategoryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_page);

        int imageResource =  Objects.requireNonNull(getIntent().getExtras()).getInt("image");
        String name = getIntent().getExtras().getString("name");

        ImageView topicImage = findViewById(R.id.topic_image);
        topicImage.setImageResource(imageResource);

        TextView topicName = findViewById(R.id.topic_name);
        topicName.setText(name);


    }
}