package com.sharifdev.torobche.model;

/**
 * A message in chats
 */
public class Message {
    public String message;
    public String sender;
    public int senderImage;

    public Message(String message, String sender, int senderImage){
        this.message = message;
        this.sender = sender;
        this.senderImage = senderImage;
    }
}
