package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.example.medicapp.model.AdviceModel;

import java.util.List;

public interface AdviceActivityView extends MvpView {

    void showToastyMessage(String message);


    void showContentNotFound();

    void hideContentNotFound();

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void loadData(List<AdviceModel> models);

    void startLoginActivityAndClearStack();
}
