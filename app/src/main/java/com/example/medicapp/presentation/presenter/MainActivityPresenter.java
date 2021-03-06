package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.presentation.view.IMainActivityView;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<IMainActivityView> {

    public void onEntryToTheDoctorClicked(){
        getViewState().setEntryToTheDoctorFragment();
    }

    public void onChatClicked(){
        //getViewState().setExerciseFragment();
        getViewState().startChatActivity();
    }

    public void onExerciseClicked(){
        getViewState().setExerciseFragment();
    }

    public void onResultsClicked(){
        getViewState().setResultsFragment();
    }

    public void onProfileClicked(){
        getViewState().setProfileFragment();
    }

    public void onActivityResult(){
        getViewState().setEntryToTheDoctorSelected();
    }

    public void onCreate(){
        getViewState().initFragments();
    }

}
