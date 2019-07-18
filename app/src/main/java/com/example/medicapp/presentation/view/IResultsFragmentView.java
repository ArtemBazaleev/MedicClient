package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.example.medicapp.model.ResultModel;

import java.util.List;

public interface IResultsFragmentView extends MvpView {

    void showTastyMessage(String message);

    void loadResults(List<ResultModel> data);

    void startActivityResultView(ResultModel model);

    void showErrorNoResults();

    void hideErrorNoResults();

    void showRefreshing();

    void hideRefreshing();

    void startLoginActivityAndClearStack();
}
