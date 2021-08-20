package com.sharifdev.torobche.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sharifdev.torobche.Activity.HistoryActivity;
import com.sharifdev.torobche.Category.CategoryRecyclerViewAdapter;
import com.sharifdev.torobche.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private final List<Integer> images;
    private final List<String> names;
    private Context context;

    public ChatAdapter(Context context, List<Integer> items, List<String> names) {
        images = items;
        this.context = context;
        this.names = names;
    }

    @NotNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        holder.mImageView.setImageResource(images.get(position));
        holder.mTextView.setText(names.get(position));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("username", names.get(position));
                intent.putExtra("image",images.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    static class Holder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;

        public Holder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.chat_image);
            mTextView = view.findViewById(R.id.chat_text);
        }
    }
}