package com.example.medicapp.model;

import android.net.Uri;

public class BaseMessage {
    public static final int MESSAGE_TYPE_SENDER = 0;
    public static final int MESSAGE_TYPE_RECIVER = 1;
    public static final int MESSAGE_TYPE_RECEIVER_PHOTO = 2;
    public static final int MESSAGE_TYPE_SENDER_IMAGE = 3;
    public int messageType = 0;
    private String message;
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseMessage() {
    }

    public BaseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}