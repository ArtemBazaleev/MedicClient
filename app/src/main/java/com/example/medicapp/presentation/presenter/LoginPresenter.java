package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.presentation.view.ILoginView;

@InjectViewState
public class LoginPresenter extends MvpPresenter<ILoginView> {

    public void onPasswordChanged(String password){}

    public void onLoginChanged(String login){}

    public void onBtnLoginClicked(){
        getViewState().startMainActivity();
    }

    public void onRegistrationClicked(){
        getViewState().startRegistrationActivity();
    }

}
