package com.sharifdev.torobche.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sharifdev.torobche.Activity.QuizActivity;
import com.sharifdev.torobche.Adapters.ProfileChooseArrayAdapter;
import com.sharifdev.torobche.Category.CategoryRecyclerViewAdapter;
import com.sharifdev.torobche.Category.SelectCategoryActivity;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.UserUtils;
import com.sharifdev.torobche.model.Question;
import com.sharifdev.torobche.model.Quiz;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizHolder> {

    private final List<Quiz> mValues;
    private Context context;
    private List<Question> questions;
    ActivityFragment fragment;

    public QuizAdapter(Context context, List<Quiz> items, List<Question> questions, ActivityFragment fragment) {
        mValues = items;
        this.context = context;
        this.questions = questions;
        this.fragment = fragment;
    }

    @Override
    public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view;
        if (viewType == R.layout.history_card) {
            view = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_card, parent, false);
        } else {
            view = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_button_layout, parent, false);
        }
        return new QuizHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == mValues.size()) ? R.layout.add_button_layout : R.layout.history_card;
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    @Override
    public void onBindViewHolder(@NotNull final QuizHolder holder, final int position) {
        if (position == mValues.size()) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, QuizActivity.class);
                    fragment.startActivityForResult(intent, 1);
                }
            });
        } else {
            holder.imageView.setImageResource(R.drawable.history);
            if (mValues.get(position).getQuestions() != null)
                holder.number.setText("Num of questions: " + mValues.get(position).getQuestions().size());
            holder.name.setText(mValues.get(position).getName());
            holder.time.setText("Time: " + String.valueOf(mValues.get(position).getTime()));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo show quiz details and edit
                }
            });
        }
    }

    public static class QuizHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView time;
        TextView number;

        public QuizHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.topic_image1);
            name = itemView.findViewById(R.id.topic_name1);
            time = itemView.findViewById(R.id.point1);
            number = itemView.findViewById(R.id.date1);
            if (imageView == null) {
                imageView = itemView.findViewById(R.id.select_category_item_image);
            }
        }
    }

    public static class QuestionArrayAdapter extends ArrayAdapter<Question> {
        List<Question> questions;

        public QuestionArrayAdapter(@NonNull Context context, int resource, List<Question> questions) {
            super(context, resource);
            this.questions = questions;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }

}
