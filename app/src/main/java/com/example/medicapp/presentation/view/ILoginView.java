package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;

public interface ILoginView extends MvpView {
    void startMainActivity(String token, String id);
    void showToastyMessage(String message);
    void startRegistrationActivity();
    void setEnabledLoginBtn(boolean enabled);
    void showLoadingIndicator();
    void hideLoadingIndicator();

    void showRestorePasswordDialog();

    void hideRestorePasswordDialog();

    void showSmsFieldDialog();

    void updateDialogModeEnterPassword();
}
