package com.example.medicapp.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.medicapp.model.BaseMessage;
import com.example.medicapp.presentation.view.IChatActivityView;

@InjectViewState
public class ChatActivityPresenter extends MvpPresenter<IChatActivityView> {
    private String message="";

    public void onSendBtnClicked(){
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setMessage(message);
        getViewState().addMessage(baseMessage);
    }

    public void onMessageChanged(String message){
        this.message = message;
    }

}
