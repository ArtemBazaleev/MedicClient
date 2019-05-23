package com.example.medicapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.medicapp.App;
import com.example.medicapp.R;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;
import com.github.nkzawa.socketio.client.Socket;

public class MessageNotificationService extends Service {
    private NotificationManager nm;
    private Socket mSocket ;
    private Handler handler;

    public int onStartCommand(Intent intent, int flags, int startId) {
        App app = (App) getApplication();
        mSocket = app.getmSocket();
        mSocket.on("new message", onNewMessage);
        Log.d(this.getClass().getName(),"OnStartCommand");
        sendNotif("qweqweq","asdasd");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(this.getClass().getName(),"OnCreate");
        handler = new Handler();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.getClass().getName(),"OnDestroy");
        mSocket.off("new message", onNewMessage);
    }
//todo fix me
    private void sendNotif(String textTitle, String textContent) {
        Log.d(this.getClass().getName(),"OnNotify");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(R.drawable.house)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Notification notification = builder.build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
        Toast.makeText(this,textTitle + ":" + textContent,Toast.LENGTH_SHORT).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Emitter.Listener onNewMessage = args -> {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(this.getClass().getName(),"OnNewMessage");
                JSONObject data = (JSONObject) args[0];
                String username;
                String message;
                try {
                    username = data.getString("username");
                    message = data.getString("message");
                } catch (JSONException e) {
                    return;
                }
                sendNotif(username,message);
            }
        });
    };
}
