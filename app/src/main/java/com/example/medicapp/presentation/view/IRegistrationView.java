package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;

public interface IRegistrationView extends MvpView {
    void showToastyMessage(String message);
    void startLoginActivity();
    void showAlertDialog();
    void showLoadingIndicator();
    void hideLoadingIndicator();
    void setEnabledSubmitBtn(boolean enabled);
}
