package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.presentation.view.IRegistrationView;

@InjectViewState
public class RegistrationPresenter extends MvpPresenter<IRegistrationView> {

    public void onLoginClicked(){
        getViewState().startLoginActivity();
    }

    public void onRegisterClicked(){}

    public void onLoginChanged(String login){}

    public void onPasswordChanged(String password){}

    public void onConfirmPasswordChanged(String confirmPassword){}
}
