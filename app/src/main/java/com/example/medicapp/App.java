package com.example.medicapp;

import android.app.Application;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.medicapp.ui.ChatActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

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
        initSocket();
    }

    public void disconnect(){
        mSocket.disconnect();
    }

    public void initSocket(){
        try {
            IO.Options mOptions = new IO.Options();
            mOptions.path = "/socstream/";
            mOptions.secure = false;
            Log.d("test", "initSocket: " + mOptions.toString());
            mSocket = IO.socket(Constants.BASE_SOCKET_URL, mOptions);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
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
