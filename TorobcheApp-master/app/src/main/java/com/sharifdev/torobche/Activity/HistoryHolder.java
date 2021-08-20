package com.sharifdev.torobche.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sharifdev.torobche.R;

public class HistoryHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView date;
    TextView topic;
    TextView point;
    //------------------


    public HistoryHolder(LinearLayout view) {
        super(view);
        imageView = view.findViewById(R.id.topic_image1);
        date = view.findViewById(R.id.date1);
        topic = view.findViewById(R.id.topic_name1);
        point = view.findViewById(R.id.point1);
        if (imageView == null) {
            imageView = view.findViewById(R.id.select_category_item_image);
        }
    }
}
