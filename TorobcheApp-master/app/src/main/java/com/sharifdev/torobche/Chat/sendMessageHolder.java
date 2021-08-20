package com.sharifdev.torobche.Chat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sharifdev.torobche.R;
import com.sharifdev.torobche.model.Message;

class SentMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText;

    SentMessageHolder(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_message_send);
    }

    void bind(Message message) {
        messageText.setText(message.message);
    }
}