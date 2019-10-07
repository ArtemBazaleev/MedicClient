package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;


public interface IRegistrationView extends MvpView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showToastyMessage(String message);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void startLoginActivity();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showAlertDialog();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLoadingIndicator();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void hideLoadingIndicator();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void setEnabledSubmitBtn(boolean enabled);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showProgressDialogWindow();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void hideProgressDialogWindow();
    @StateStrategyType(OneExecutionStateStrategy.class)
    void setMoreSmsText(String text);
}
