package com.sharifdev.torobche.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.sharifdev.torobche.Category.CategoryRecyclerViewAdapter;
import com.sharifdev.torobche.Category.SelectCategoryActivity;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.CategoryUtils;
import com.sharifdev.torobche.model.History;
import com.sharifdev.torobche.model.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<HistoryHolder> {

    private final List<Question> mValues;
    private Context context;
    private ActivityFragment fragment;

    public QuestionAdapter(Context context, List<Question> items, ActivityFragment fragment) {
        mValues = items;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view;
        if (viewType == R.layout.single_image_layout) {
            view = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_card, parent, false);
        } else {
            view = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_button_layout, parent, false);
        }
        return new HistoryHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == mValues.size()) ? R.layout.add_button_layout : R.layout.single_image_layout;
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    @Override
    public void onBindViewHolder(final HistoryHolder holder, final int position) {
        if (position == mValues.size()) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, QuestionMakeActivity.class);
                    fragment.startActivityForResult(intent, 1);
                }
            });
        } else {
            int imageByName = CategoryUtils.getCategoryImageByName(mValues.get(position).getTopic());
            holder.imageView.setImageResource(imageByName);
            holder.point.setText(mValues.get(position).getText());
            holder.topic.setText(mValues.get(position).getTopic());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo show question
                    Intent intent = new Intent(context, QuestionMakeActivity.class);
                    intent.putExtra("questionId", mValues.get(position).getObjId());
                    fragment.startActivityForResult(intent, 1);
                }
            });
        }
    }

}
