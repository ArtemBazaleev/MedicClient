package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.ExerciseModel;
import com.example.medicapp.presentation.view.IExerciseView;

@InjectViewState
public class ExercisePresenter extends MvpPresenter<IExerciseView> {

    public void onExerciseClicked(ExerciseModel data){

    }

    private void provideData(){

    }

    public void onViewCreated(){
    }

}
