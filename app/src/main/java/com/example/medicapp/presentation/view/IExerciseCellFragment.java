package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.example.medicapp.model.ExerciseModel;

import java.util.List;

public interface IExerciseCellFragment extends MvpView {
    void showToastyMessage(String message);

    void loadExerciseModels(List<ExerciseModel> data);

    void addExerciseModels(List<ExerciseModel> data);

    void startVideoViewActivity(String url);

    void showLoadingIndicator();

    void hideLoadingIndicator();
}
