package com.example.medicapp;

import android.app.Application;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class App extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String mToken = "";

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Socket getmSocket(){
        return mSocket;
    }
}
