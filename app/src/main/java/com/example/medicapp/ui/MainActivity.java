package com.example.medicapp.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

public class MainActivity extends MvpAppCompatActivity implements IMainActivityView {
    public static final int RESULT_CHAT = 6458;
    private static final String CHANNEL_ID = "ClientChanel";

    private Fragment entryToTheDoctorFragment;
    private Fragment exerciseFragment;
    private Fragment profileFragment;
    private Fragment resultFragment;

    private Fragment activeFragment;
    private FragmentManager fm;

    private BottomNavigationView navigation;
    private int notificationId = 0;

    private Socket mSocket;

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
                        return true;
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
        navigation = findViewById(R.id.navigation);
        //initSocket();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        presenter.onCreate();
    }

    private void initSocket() {
        App app = (App)getApplication();
        mSocket = app.getmSocket();
        if (!mSocket.connected())
            mSocket.connect();
        JSONObject data = new JSONObject();
        try {
            data.put("userId", app.getmUserID());
            data.put("token", app.getmToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("auth", data);
        mSocket.on("newMessage",newMessage);    }

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
        startActivityForResult(new Intent(MainActivity.this, ChatActivity.class),RESULT_CHAT);
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

    private Emitter.Listener newMessage = args -> this.runOnUiThread(() -> {
        try {
        JSONObject data = (JSONObject) args[0];
        createNotificationChannel();
        showNotif("Врач","Сообщение:",data.getJSONObject("message").getString("message"));

//            BaseMessage baseMessage = new BaseMessage();
//            baseMessage.messageType = BaseMessage.MESSAGE_TYPE_RECIVER;
//            baseMessage.setMessage(data.getJSONObject("message").getString("message"));
//            //baseMessage.setTime(data.getJSONObject("message").getLong("date"));
//            if (!data.getJSONObject("message").getString("message").equals(app.getmUserID())) {
//                adapter.addMessage(baseMessage);
//                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
//            }
        } catch (Exception e) {
            e.printStackTrace();
       }
    });


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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(contentTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
