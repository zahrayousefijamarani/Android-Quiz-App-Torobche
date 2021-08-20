package com.sharifdev.torobche.Chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.ml.Utils;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.model.Message;

class ReceiveMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, nameText;
    ImageView profileImage;

    public ReceiveMessageHolder(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
    }

    void bind(Message message) {
        messageText.setText(message.message);

        // Format the stored timestamp into a readable String using method.
        nameText.setText(message.sender);

        // Insert the profile image from the URL into the ImageView.
        profileImage.setImageResource(message.senderImage);
    }
}