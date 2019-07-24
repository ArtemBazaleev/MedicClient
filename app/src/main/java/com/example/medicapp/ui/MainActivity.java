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
    private boolean isInBackground = true;

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
                        return false;
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
        app.setDialogID("");
        //initSocket();
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
        if (hasChatDialog) {
            changeChatIconNoMessages();
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        }
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
                .hide(entryToTheDoctorFragment)
                .commit();
        fm.beginTransaction()
                .add(R.id.fragment_container, exerciseFragment, "2")
                .commit();
        fm.beginTransaction()
                .add(R.id.fragment_container, resultFragment, "3")
                .hide(resultFragment)
                .commit();
        fm.beginTransaction()
                .add(R.id.fragment_container, profileFragment, "4")
                .hide(profileFragment)
                .commit();
        activeFragment = exerciseFragment;
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

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: called");
        super.onStart();
        isInBackground = false;
        if (app.getmToken().equals("") || app.getmUserID().equals("")){
            finish();
        }
        initSocket();
    }

    @Override
    protected void onPause(){
        super.onPause();
        isInBackground = true;
        Log.d(TAG, "onPause: _______________________________________________________________");
        offSocket();
//        mSocket.disconnect();
        Log.d(TAG, "onPause: soket:" + mSocket.connected());
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //test
        offSocket();
        mSocket.disconnect();
        if (mSocket.hasListeners("newMessage"))
            mSocket.off("newMessage");
    }

    private void offSocket(){
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectedError);
        mSocket.off(Socket.EVENT_CONNECT, onConnected);
        mSocket.off("authOk",authOk);
        //mSocket.off("newMessage",newMessage);
        mSocket.off("error-pipe",error_pipe);
        Log.d(TAG, "offSocket: called");
    }
    private void onSocket(){
        if (!mSocket.hasListeners("authOk"))
            mSocket.on("authOk",authOk);
        if (!mSocket.hasListeners("newMessage"))
            mSocket.on("newMessage",newMessage);
        if (!mSocket.hasListeners("error-pipe"))
            mSocket.on("error-pipe",error_pipe);
    }



    ///test


    private void initSocket() {
        app.initSocket();
        mSocket = app.getmSocket();
        //mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectedError);
        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        mSocket.on(Socket.EVENT_DISCONNECT, ondisconnect);
        //offSocket();
        onSocket();
        if (mSocket.connected()) {
            Log.d(TAG, "Connected !!");
            onSocket();
            JSONObject data = new JSONObject();
            try {
                data.put("userId", app.getmUserID());
                data.put("token", app.getmToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Emitting...", "initSocket: " + data.toString());
            mSocket.emit("auth", data);
        }
        else{
            mSocket.connect();
        }
        Log.d(TAG, "initSocket soket: " + mSocket.connected());
    }


    //events

    private Emitter.Listener error_pipe  = args -> MainActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        Log.d(TAG,"error_pipe  "+ data.toString());
    });

    private Emitter.Listener newMessage = args -> MainActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        if (isInBackground)
            return;
        Log.d(TAG,"newMessage  "+ data.toString());
        try {
            app.vibrate();
            changeChatIconHaveMessage();
        } catch (Exception e) {
            e.printStackTrace();
       }
    });

    private void changeChatIconHaveMessage() {
        navigation.getMenu().findItem(R.id.navigation_chat).setIcon(R.drawable.ic_lightbulb);
    }
    private void changeChatIconNoMessages(){
        navigation.getMenu().findItem(R.id.navigation_chat).setIcon(R.mipmap.chat_icon);
    }

    private Emitter.Listener authOk = args -> MainActivity.this.runOnUiThread(() -> {
        Log.d(TAG, "AuthOK: called!!");
        try {
            JSONObject data = (JSONObject) args[0];
            Log.d( TAG ,"AuthOk: " + data.toString());
            if (!data.getJSONArray("dialogs").getJSONObject(0).has("id")) { // NO CHAT
                hasChatDialog = false;
                return;
            }
            if (data.getJSONArray("dialogs").getJSONObject(0).has("unreadMessages")) {
                if (data.getJSONArray("dialogs").getJSONObject(0).getInt("unreadMessages") != 0)
                    changeChatIconHaveMessage();
                else changeChatIconNoMessages(); // на всякий случай
            }
            else changeChatIconNoMessages();
            String dialogID = data.getJSONArray("dialogs").getJSONObject(0).getString("id");
            app.setDialogID(dialogID);
            hasChatDialog = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    private Emitter.Listener onConnected = args -> MainActivity.this.runOnUiThread(() -> {
        Log.d(TAG, "onConnected: called");
        Log.d(TAG, "onConnected socket: "+ mSocket.connected());
        JSONObject data = new JSONObject();
        try {
            data.put("userId", app.getmUserID());
            data.put("token", app.getmToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Emitting...", "initSocket: " + data.toString());
        mSocket.emit("auth", data);
    });

    private Emitter.Listener onConnectedError = args -> MainActivity.this.runOnUiThread(() -> {
        Log.d(TAG, "onConnectedError: called");
        //Toast.makeText(this, "ConnectionError", Toast.LENGTH_SHORT).show();
    });


    private Emitter.Listener ondisconnect = args -> MainActivity.this.runOnUiThread(() -> {
        Log.d(TAG, "ondisconnect: called");
    });
}
