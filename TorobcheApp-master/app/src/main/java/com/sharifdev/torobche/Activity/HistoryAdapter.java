package com.sharifdev.torobche.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.CategoryUtils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {

    private final List<HistoryValue> mValues;
    private Context context;

    public HistoryAdapter(Context context, List<HistoryValue> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view;
        view = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card, parent, false);

        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(final HistoryHolder holder, final int position) {
        holder.imageView.setImageResource(CategoryUtils.getCategoryImageByName(mValues.get(position).topicTxt));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo go to history page
                /*Intent intent = new Intent(context, HistoryActivity.class);
                intent.putExtra("history_number", mValues.get(position));
                context.startActivity(intent);*/
            }
        });
        holder.point.setText("Score: " + String.valueOf(mValues.get(position).pointNum));
        holder.topic.setText(mValues.get(position).topicTxt);
        holder.date.setText(mValues.get(position).dateTxt);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class HistoryValue {
        String dateTxt;
        String topicTxt;
        int pointNum;

        public HistoryValue(int pointNum, String dateTxt, String topicTxt) {
            this.pointNum = pointNum;
            this.dateTxt = dateTxt;
            this.topicTxt = topicTxt;
        }
    }


}
