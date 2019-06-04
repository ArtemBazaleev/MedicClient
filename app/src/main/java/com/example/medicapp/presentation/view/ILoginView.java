package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;

public interface ILoginView extends MvpView {
    void startMainActivity(String token);
    void showToastyMessage(String message);
    void startRegistrationActivity();
}
