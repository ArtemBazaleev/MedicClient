package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface ILoginView extends MvpView {
    void startMainActivity(String token, String id);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showToastyMessage(String message);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void startRegistrationActivity();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void setEnabledLoginBtn(boolean enabled);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLoadingIndicator();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void hideLoadingIndicator();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showRestorePasswordDialog();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void hideRestorePasswordDialog();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showSmsFieldDialog();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void updateDialogModeEnterPassword();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showProgressDialog();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void hideProgressDialog();
}
