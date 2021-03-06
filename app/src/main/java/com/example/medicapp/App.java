package com.example.medicapp;

import android.app.Application;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
    private boolean inited = false;
    private Vibrator vibrator;
    private String dialogID = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //initSocket();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    public void disconnect(){
        mSocket.disconnect();
    }

    public void initSocket(){
        if (inited)
            return;
        Log.d("Application", "initSocket: called!");
        try {
            IO.Options mOptions = new IO.Options();
            mOptions.path = "/socstream/";
            mOptions.secure = false;
            mOptions.forceNew = true; //added
            Log.d("test", "initSocket: " + mOptions.toString());
            mSocket = IO.socket(Constants.BASE_SOCKET_URL, mOptions);
            inited = true;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void forceInit(){
        if (!mSocket.connected()){
            inited = false;
            mSocket.off();
            mSocket.close();
            mSocket.disconnect();
            mSocket = null;
            initSocket();
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

    public void vibrate(){
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
    }

    public String getDialogID() {
        return dialogID;
    }

    public void setDialogID(String dialogID) {
        this.dialogID = dialogID;
    }
}
