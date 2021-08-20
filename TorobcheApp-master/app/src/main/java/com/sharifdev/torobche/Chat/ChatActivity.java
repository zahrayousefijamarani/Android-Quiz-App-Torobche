package com.sharifdev.torobche.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sharifdev.torobche.R;
import com.sharifdev.torobche.backUtils.UserUtils;
import com.sharifdev.torobche.model.Chat;
import com.sharifdev.torobche.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    String username;
    int imageResource;
    String thisUserName;
    public ArrayList<Message> messages = new ArrayList<>();
    public ParseObject chat;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        progressBar = findViewById(R.id.load_chat);

        username = Objects.requireNonNull(getIntent().getExtras()).getString("username");
        imageResource = Objects.requireNonNull(getIntent().getExtras()).getInt("image");
        thisUserName = ParseUser.getCurrentUser().getUsername();

        // get chat
        progressBar.setVisibility(View.VISIBLE);
        UserUtils.getUserChat(username, this);


    }

    public void setChat() {
        RecyclerView messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        final MessageListAdapter messageAdapter = new MessageListAdapter(this, messages);
        messageAdapter.setCurrentUserName(thisUserName);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);
        final EditText message = findViewById(R.id.message);

        Button send = findViewById(R.id.send_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.add(new Message(message.getText().toString(), thisUserName, imageResource));
                messageAdapter.notifyDataSetChanged();
                // save it
                UserUtils.sendMsg(message.getText().toString(), username, ChatActivity.this);

                message.getText().clear();
            }
        });
    }


}