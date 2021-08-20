package com.sharifdev.torobche.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharifdev.torobche.R;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private final List<SelectCategoryActivity.HolderClass> items;
    private Context context ;

    public CategoryAdapter(List<SelectCategoryActivity.HolderClass> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        TextView textView;

        if (view == null) {
            LayoutInflater inflter = (LayoutInflater.from(context));
            view = inflter.inflate(R.layout.select_category_item, null);
        }

        imageView = (ImageView) view.findViewById(R.id.select_category_item_image);
        textView = (TextView) view.findViewById(R.id.select_category_item_text);
        imageView.setImageResource(items.get(i).image);
        textView.setText(items.get(i).name);
        return view;
    }
}