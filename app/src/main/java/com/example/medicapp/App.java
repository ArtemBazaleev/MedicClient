package com.example.medicapp;

import android.app.Application;
import android.os.CountDownTimer;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class App extends Application implements ITimerSms {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private CountDownTimer timer;
    private boolean isTicking = false;
    private ITimerListener listener;


    private String mToken = "";

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
    private String mUserID = "";

    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Socket getmSocket(){
        return mSocket;
    }

    @Override
    public void startTimer(long mils) {
        isTicking = true;
        timer = new CountDownTimer(mils, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                listener.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                listener.onTimerFinished();
            }
        };
        timer.start();
    }

    @Override
    public void stopTimer() {
        if (isTicking) {
            timer.cancel();
            isTicking = false;
        }
    }

    @Override
    public void setTimerListener(ITimerListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isTicking() {
        return isTicking;
    }
}
