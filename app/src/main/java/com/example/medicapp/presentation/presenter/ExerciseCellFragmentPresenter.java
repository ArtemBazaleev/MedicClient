package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.ExerciseModel;
import com.example.medicapp.presentation.view.IExerciseCellFragment;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class ExerciseCellFragmentPresenter extends MvpPresenter<IExerciseCellFragment> {

    public void onViewCreated(){
        provideData();
    }

    private void provideData(){
        List<ExerciseModel> models = new ArrayList<>();
        for (int i = 0; i<10; i++)
            models.add(new ExerciseModel());
        getViewState().loadExerciseModels(models);
    }

    public void loadMoreEllements(){

    }

    public void onItemClicked(ExerciseModel exerciseModel){
        getViewState().startVideoViewActivity();
    }
}
