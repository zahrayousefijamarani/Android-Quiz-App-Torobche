package com.sharifdev.torobche.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sharifdev.torobche.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileChooseArrayAdapter extends ArrayAdapter<String> {
    private List<Integer> avatars;

    public ProfileChooseArrayAdapter(Context context, List<String> items, List<Integer> images) {
        super(context, R.layout.select_profile_item, items);
        this.avatars = images;
    }

    public ProfileChooseArrayAdapter(Context context, String[] items, Integer[] images) {
        super(context, R.layout.select_profile_item, items);
        this.avatars = Arrays.asList(images);
    }

    public ProfileChooseArrayAdapter
            (Context context, int items, int images) {
        super(context, R.layout.select_profile_item, ((String[]) context.getResources().getTextArray(items)));

        final TypedArray imgs = context.getResources().obtainTypedArray(images);
        this.avatars = new ArrayList<Integer>() {{
            for (int i = 0; i < imgs.length(); i++) {
                add(imgs.getResourceId(i, -1));
            }
        }};

        // recycle the array
        imgs.recycle();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(avatars.get(position), 0, 0, 0);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(avatars.get(position), 0, 0, 0);
        }
        textView.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
        return view;
    }
}
