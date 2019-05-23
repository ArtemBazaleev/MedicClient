package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.medicapp.R;
import com.example.medicapp.service.MessageNotificationService;

public class MainActivity extends AppCompatActivity {
    public static final int RESULT_CHAT = 6458;
    private TextView mTextMessage;

    private BottomNavigationView navigation;
    private int currentPage = R.id.navigation_home;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (currentPage ==  R.id.navigation_home)
                            return true;
                        replaceFragment(new EntryToTheDoctorFragment());
                        currentPage = R.id.navigation_home;
                        return true;
                    case R.id.navigation_exercise:
                        if (currentPage == R.id.navigation_exercise)
                            return true;
                        replaceFragment(new ExerciseFragment());
                        currentPage =R.id.navigation_exercise;
                        return true;
                    case R.id.navigation_chat:
                        startActivityForResult(new Intent(MainActivity.this, ChatActivity.class),RESULT_CHAT);
                        currentPage = R.id.navigation_chat;
                        return true;
                    case R.id.navigation_results:
                        if (currentPage == R.id.navigation_results)
                            return true;
                        replaceFragment(new ResultsFragment());
                        currentPage = R.id.navigation_results;
                        return true;
                    case R.id.navigation_profile:
                        if (currentPage == R.id.navigation_profile)
                            return true;
                        replaceFragment(new ProfileFragment());
                        currentPage = R.id.navigation_profile;
                        return true;
                }
                return false;
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        replaceFragmentWithStack(new EntryToTheDoctorFragment());
    }

    public void replaceFragmentWithStack(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    public void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_CHAT){
           navigation.setSelectedItemId(R.id.navigation_home);
           currentPage = R.id.navigation_home;
        }

    }
}
