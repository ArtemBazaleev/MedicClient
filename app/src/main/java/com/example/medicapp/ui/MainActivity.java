package com.example.medicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.medicapp.R;
import com.example.medicapp.presentation.presenter.MainActivityPresenter;
import com.example.medicapp.presentation.view.IMainActivityView;

public class MainActivity extends MvpAppCompatActivity implements IMainActivityView {
    public static final int RESULT_CHAT = 6458;

    private Fragment entryToTheDoctorFragment;
    private Fragment exerciseFragment;
    private Fragment profileFragment;
    private Fragment resultFragment;

    private Fragment activeFragment;
    private FragmentManager fm;

    private BottomNavigationView navigation;

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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        presenter.onCreate();
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
    }

    @Override
    public void setExerciseFragment() {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(exerciseFragment)
                .commit();
        activeFragment  = exerciseFragment;
    }

    @Override
    public void setResultsFragment() {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(resultFragment)
                .commit();
        activeFragment  = resultFragment;
    }

    @Override
    public void setProfileFragment() {
        fm.beginTransaction()
                .hide(activeFragment)
                .show(profileFragment)
                .commit();
        activeFragment  = profileFragment;
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
}
