package com.example.medicapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.App;
import com.example.medicapp.Constants;
import com.example.medicapp.R;
import com.example.medicapp.presentation.presenter.MainActivityPresenter;
import com.example.medicapp.presentation.view.IMainActivityView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

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
    Badge badge;

    Timer timer = new Timer ();
    @BindView(R.id.bnve) BottomNavigationViewEx bnv;

    private Socket mSocket;
    private boolean inited = false;
    private boolean hasChatDialog = false;
    private boolean isInBackground = true;
    private int unreadMessagesCount = 0;

    @InjectPresenter
    MainActivityPresenter presenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        presenter.onEntryToTheDoctorClicked();
                        setColoredBNV(3);
                        return true;
                    case R.id.navigation_exercise:
                        presenter.onExerciseClicked();
                        setColoredBNV(0);
                        return true;
                    case R.id.navigation_chat:
                        presenter.onChatClicked();
                        //setColoredBNV(2);
                        return false;
                    case R.id.navigation_results:
                        presenter.onResultsClicked();
                        setColoredBNV(1);
                        return true;
                    case R.id.navigation_profile:
                        presenter.onProfileClicked();
                        setColoredBNV(4);
                        return true;
                }
                return false;
            };

    private void setColoredBNV(int position){
        for (int i = 0; i<5; i++){
            if (i == position){
                bnv.setIconTintList(position,ColorStateList.valueOf(getResources().getColor(R.color.colorTest1)));
                bnv.setTextTintList(position,ColorStateList.valueOf(getResources().getColor(R.color.colorTest1)));
            }
            else{
                bnv.setIconTintList(i,ColorStateList.valueOf(getResources().getColor(R.color.black_overlay_bnv)));
                bnv.setTextTintList(i,ColorStateList.valueOf(getResources().getColor(R.color.black_overlay_bnv)));
            }
        }
    }
    private Badge addBadgeAt(int position, int number) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(bnv.getBottomNavigationItemView(position));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bnv.enableAnimation(false);
        bnv.enableShiftingMode(false);
        bnv.setTextSize(11f);
        bnv.enableItemShiftingMode(false);
        badge = addBadgeAt(2, unreadMessagesCount);
        presenter.onCreate();
        app = (App)getApplication();
        app.setDialogID("");
        //initializeSocket();
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
            unreadMessagesCount = 0;
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
        setColoredBNV(0);
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
        bnv.setSelectedItemId(R.id.navigation_home);
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
        Log.d(TAG, "onStart: called_________________________________________________________");
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
        mSocket.disconnect();
        mSocket.off();
        timer.cancel();
        inited = false;
        unreadMessagesCount = 0;
        changeChatIconNoMessages();
        //app.forceInit();
        //mSocket.off();
        //Log.d(TAG, "onPause: soket:" + mSocket.connected());
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //test
//        offSocket();
//        mSocket.disconnect();
//        if (mSocket.hasListeners("newMessage"))
//            mSocket.off("newMessage");
    }

    private void offSocket(){
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectedError);
        mSocket.off(Socket.EVENT_CONNECT, onConnected);
        mSocket.off(Socket.EVENT_DISCONNECT, ondisconnect);
        mSocket.off("authOk",authOk);
        mSocket.off("newMessage",newMessage);
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
        if (!mSocket.hasListeners("getDialogs"))
            mSocket.on("dialogList", dialogList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (app.getmToken().equals("") || app.getmUserID().equals("")){
            Log.d(TAG, "onResume: emptyData ------------------------------------------------");
            finish();
        }
    }

    ///test


    private void initSocket() {
        initializeSocket();
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
        Log.d("error_pipe", data.toString());
        try{
            if (data.has("code")){
                String s = data.getString("code");
                if ("403".equals(s)) {
                    Toast.makeText(MainActivity.this, data.getString("message"),Toast.LENGTH_SHORT).show();
                    timer.cancel();
                    inited = false;
                    startLoginActivityAndClearStack();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    });
    public void startLoginActivityAndClearStack() {
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private Emitter.Listener newMessage = args -> MainActivity.this.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        if (isInBackground)
            return;
        Log.d(TAG,"newMessage  "+ data.toString());
        try {
            app.vibrate();
            unreadMessagesCount++;
            changeChatIconHaveMessage(unreadMessagesCount);
        } catch (Exception e) {
            e.printStackTrace();
       }
    });


    private void changeChatIconHaveMessage(int count) {
        Log.d(TAG, "changeChatIconHaveMessage: called");
        unreadMessagesCount = count;
        badge.setBadgeNumber(count);
    }
    private void changeChatIconNoMessages(){
        Log.d(TAG, "changeChatIconNoMessages: Called ");
        badge.hide(false);
        bnv.getMenu().findItem(R.id.navigation_chat).setIcon(R.mipmap.chat_icon);
    }

    private Emitter.Listener authOk = args -> MainActivity.this.runOnUiThread(() -> {
        Log.d(TAG, "AuthOK: called!!");
        inited = true;
        timer.cancel();
        try {
            JSONObject data = (JSONObject) args[0];
            Log.d( TAG ,"AuthOk: " + data.toString());
            if (!data.getJSONArray("dialogs").getJSONObject(0).has("id")) { // NO CHAT
                hasChatDialog = false;
                return;
            }
            if (data.getJSONArray("dialogs").getJSONObject(0).has("unreadMessages")) {
                if (data.getJSONArray("dialogs").getJSONObject(0).getInt("unreadMessages") != 0)
                    changeChatIconHaveMessage((data.getJSONArray("dialogs").getJSONObject(0).getInt("unreadMessages")));
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
        Log.d(TAG, "onConnected: authOk = " + mSocket.hasListeners("authOk"));
        if (!mSocket.hasListeners("authOk"))
            mSocket.on("authOk", authOk);

        Thread t = new Thread(){
            public void run() {
                Log.d("Emitting...", "initSocket: " + data.toString());
                mSocket.emit("auth", data, (Ack) args1 -> Log.e(TAG, "++++++++++++++++++++++++ACK"));
            }
        };
        t.start();

        if (!inited)
            requestDialogs();
    });

    private void  requestDialogs() {
        timer = new Timer();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                if (inited)
                    timer.cancel();
                Log.d(TAG, "requesting dialogs................................................: ");
                mSocket.emit("getDialogs");

            }
        };
        timer.schedule (hourlyTask, 0L, 1000);

    }

    private Emitter.Listener onConnectedError = args -> MainActivity.this.runOnUiThread(() -> {
        Log.d(TAG, "onConnectedError: called");
        //Toast.makeText(this, "ConnectionError", Toast.LENGTH_SHORT).show();
    });


    private Emitter.Listener ondisconnect = args -> MainActivity.this.runOnUiThread(() -> {
        Log.d(TAG, "ondisconnect: called");
    });

    private Emitter.Listener dialogList = args -> MainActivity.this.runOnUiThread(()->{
        JSONObject data = (JSONObject) args[0];
        Log.d(TAG, "DialogListReceived: " + data.toString());
        inited = true;
        try {
            if (!data.getJSONArray("dialogs").getJSONObject(0).has("id")) { // NO CHAT
                hasChatDialog = false;
                return;
            }
            if (data.getJSONArray("dialogs").getJSONObject(0).has("unreadMessages")) {
                if (data.getJSONArray("dialogs").getJSONObject(0).getInt("unreadMessages") != 0)
                    changeChatIconHaveMessage(data.getJSONArray("dialogs").getJSONObject(0).getInt("unreadMessages"));
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

    private void initializeSocket(){
        try {
            IO.Options mOptions = new IO.Options();
            mOptions.path = "/socstream/";
            mOptions.secure = false;
            mOptions.forceNew = true; //added
            mOptions.reconnection = true;
            Log.d("test", "initSocket: " + mOptions.toString());
            mSocket = IO.socket(Constants.BASE_SOCKET_URL, mOptions);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
