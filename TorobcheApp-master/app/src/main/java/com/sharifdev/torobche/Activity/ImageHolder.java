package com.sharifdev.torobche.Activity;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.sharifdev.torobche.R;

public class ImageHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;

    public ImageHolder(LinearLayout view) {
        super(view);
        mImageView = view.findViewById(R.id.category_image);
        if (mImageView == null)
            mImageView = view.findViewById(R.id.select_category_item_image);
    }

}
