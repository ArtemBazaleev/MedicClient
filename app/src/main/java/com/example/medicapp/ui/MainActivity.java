package com.example.medicapp.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.App;
import com.example.medicapp.R;
import com.example.medicapp.presentation.presenter.MainActivityPresenter;
import com.example.medicapp.presentation.view.IMainActivityView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

public class MainActivity extends MvpAppCompatActivity implements IMainActivityView {
    public static final int RESULT_CHAT = 6458;
    private static final String CHANNEL_ID = "ClientChanel";
    private static final String TAG = "MainActivity" ;

    private Fragment entryToTheDoctorFragment;
    private Fragment exerciseFragment;
    private Fragment profileFragment;
    private Fragment resultFragment;

    private Fragment activeFragment;
    private FragmentManager fm;

    private BottomNavigationView navigation;
    private int notificationId = 0;
    private App app;

    private Socket mSocket;
    private boolean inited = false;
    private boolean hasChatDialog = false;

    @InjectPresenter
    MainActivityPresenter presenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        presenter.onEntryToTheDoctorClicked();
                        return true;
                    case R.id.navigation_exercise:
                        presenter.onExerciseClicked();
                        return true;
                    case R.id.navigation_chat:
                        presenter.onChatClicked();
                        return hasChatDialog;
                    case R.id.navigation_results:
                        presenter.onResultsClicked();
                        return true;
                    case R.id.navigation_profile:
                        presenter.onProfileClicked();
                        return true;
                }
                return false;
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        presenter.onCreate();
        app = (App)getApplication();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_CHAT) {
            presenter.onActivityResult();
        }
    }

    //MVP
    @Override
    public void showToastyMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEntryToTheDoctorFragment() {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(entryToTheDoctorFragment)
                .commit();
        activeFragment  = entryToTheDoctorFragment;
        hideKeyboard();
    }

    @Override
    public void setExerciseFragment() {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(exerciseFragment)
                .commit();
        activeFragment  = exerciseFragment;
        hideKeyboard();
    }

    @Override
    public void setResultsFragment() {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(resultFragment)
                .commit();
        activeFragment  = resultFragment;
        hideKeyboard();
    }

    @Override
    public void setProfileFragment() {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(profileFragment)
                .commit();
        activeFragment  = profileFragment;
        hideKeyboard();
    }

    @Override
    public void startChatActivity() {
        if (hasChatDialog)
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        else  showToastyMessage("Диалог с врачем не обнаружен");
    }

    @Override
    public void initFragments() {
        fm = getSupportFragmentManager();
        entryToTheDoctorFragment = new EntryToTheDoctorFragment();
        exerciseFragment = new ExerciseFragment();
        profileFragment = new ProfileFragment();
        resultFragment = new ResultsFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, entryToTheDoctorFragment,"1")
                .commit();
        fm.beginTransaction()
                .add(R.id.fragment_container, exerciseFragment, "2")
                .hide(exerciseFragment)
                .commit();
        fm.beginTransaction()
                .add(R.id.fragment_container, resultFragment, "3")
                .hide(resultFragment)
                .commit();
        fm.beginTransaction()
                .add(R.id.fragment_container, profileFragment, "4")
                .hide(profileFragment)
                .commit();
        activeFragment = entryToTheDoctorFragment;
    }

    @Override
    public void setEntryToTheDoctorSelected() {
        navigation.setSelectedItemId(R.id.navigation_home);
    }
    //MVP

    void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Kenilab";
            String description = "MedicApp";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotif(String title, String contentTitle, String contentText){
        Intent intent = new Intent(this, ChatActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(contentTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(alarmSound)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: called");
        super.onStart();
        if (inited)
            return;
        if (app.getmToken().equals("") || app.getmUserID().equals("")){
            finish();
        }
        app.initSocket();
        initSocket();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause: _______________________________________________________________");
        offSocket();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();

        //test
        offSocket();
        //test
        App app = (App) getApplication();
        app.disconnect();
    }

    private void offSocket(){
        mSocket.off("authOk",authOk);
        //mSocket.off("enteredDialog",enteredDialog);
        mSocket.off("messageReceive",messageReceive);
        mSocket.off("leavedDialog",leavedDialog);
        mSocket.off("newMessage",newMessage);
        mSocket.off("messageListReceive",messageListReceive);
        mSocket.off("error-pipe",error_pipe);
        Log.d(TAG, "offSocket: called");
        mSocket.disconnect();
        inited = false;
    }




    ///test


    private void initSocket() {
        mSocket = app.getmSocket();
        if (mSocket.connected())
            Toast.makeText(this, "Connected!!", Toast.LENGTH_SHORT).show();
        else mSocket.connect();
        JSONObject data = new JSONObject();
        try {
            data.put("userId", app.getmUserID());
            data.put("token", app.getmToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectedError);
        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        mSocket.on("authOk", authOk);
        mSocket.on("messageReceive",messageReceive);
        mSocket.on("leavedDialog",leavedDialog);
        mSocket.on("newMessage",newMessage);
        mSocket.on("messageListReceive", messageListReceive);
        mSocket.on("error-pipe", error_pipe);
        Log.d("", "initSocket: " + data.toString());
        mSocket.emit("auth", data);
        inited = true;
    }


    //events

    private Emitter.Listener error_pipe  = args -> MainActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Log.d(TAG,"error_pipe  "+ data.toString());
    });

    private Emitter.Listener messageListReceive = args -> MainActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Log.d(TAG,"messageListReceive  "+ data.toString());
    });

    private Emitter.Listener newMessage = args -> MainActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Log.d(TAG,"newMessage  "+ data.toString());
        try {
            createNotificationChannel();
            app.vibrate();
            showNotif("Врач","Сообщение:", data.getJSONObject("message").getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
       }
    });

    private Emitter.Listener leavedDialog = args -> MainActivity.this.runOnUiThread(() -> {
    });

    private Emitter.Listener messageReceive  = args -> MainActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Log.d(TAG,"messageReceive " + data.toString());

    });


    private Emitter.Listener authOk = args -> MainActivity.this.runOnUiThread(() -> {
        try {
            JSONObject data = (JSONObject) args[0];
            Log.d( TAG ,"AuthOk: " + data.toString());
            JSONObject emitObj = new JSONObject();
            if (!data.getJSONArray("dialogs").getJSONObject(0).has("id")) { // NO CHAT
                hasChatDialog = false;
                return;
            }
//            if (data.getJSONArray("dialogs").getJSONObject(0).has("unreadMessages"))
//                Toast.makeText(this, data.getJSONArray("dialogs").getJSONObject(0).getInt("unreadMessages"), Toast.LENGTH_SHORT).show();
            String dialogID = data.getJSONArray("dialogs").getJSONObject(0).getString("id");
            emitObj.put("dialogId", dialogID);
            hasChatDialog = true;
            Log.d("", data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private Emitter.Listener onConnected = args -> MainActivity.this.runOnUiThread(() -> {

    });

    private Emitter.Listener onConnectedError = args -> MainActivity.this.runOnUiThread(() -> {

    });


}
