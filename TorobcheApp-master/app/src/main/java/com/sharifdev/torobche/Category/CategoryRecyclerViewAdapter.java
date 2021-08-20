package com.sharifdev.torobche.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sharifdev.torobche.HomeFragment;
import com.sharifdev.torobche.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<SelectCategoryActivity.HolderClass> mValues;
    private Context context;
    private HomeFragment fragment;

    public CategoryRecyclerViewAdapter(Context context, List<SelectCategoryActivity.HolderClass> items,
                                       HomeFragment fragment) {
        mValues = items;
        this.context = context;
        this.fragment = fragment;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view;
        if (viewType == R.layout.user_category_item) {
            view = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_category_item, parent, false);
        } else {
            view = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_button_layout, parent, false);
        }
        return new ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == mValues.size()) ? R.layout.add_button_layout : R.layout.user_category_item;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == mValues.size()) {
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SelectCategoryActivity.class);
                    fragment.startActivityForResult(intent, 1);
                }
            });
        } else {
            holder.mImageView.setImageResource(mValues.get(position).image);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mValues.get(position).name.equals("Loading..."))
                        return;
                    Intent intent = new Intent(context, CategoryPage.class);
                    intent.putExtra("image", mValues.get(position).image);
                    intent.putExtra("name", mValues.get(position).name);
                    context.startActivity(intent);
                }
            });
            holder.mText.setText(mValues.get(position).name);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText;

        public ViewHolder(LinearLayout view) {
            super(view);
            mImageView = view.findViewById(R.id.select_category_item_image);
            mText = view.findViewById(R.id.select_category_item_text);
        }
    }
}
