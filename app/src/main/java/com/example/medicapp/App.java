package com.example.medicapp;

import android.app.Application;
import android.os.CountDownTimer;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class App extends Application implements ITimerSms {

    private Socket mSocket;

    private CountDownTimer timer;
    private boolean isTicking = false;
    private ITimerListener listener;
    private String mToken = "";
    private String mUserID = "";

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            mSocket = IO.socket(Constants.SOCKET_IO);
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
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


    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }


    public String getmUserID() {
        return mUserID;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public Socket getmSocket(){
        return mSocket;
    }


}
