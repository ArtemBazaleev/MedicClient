package com.example.medicapp.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.example.medicapp.BaseMessage;

import java.util.List;

public interface IChatActivityView extends MvpView {
    void hideAll();
    void addMessage(BaseMessage message);
    void initRecycler(List<BaseMessage> messages);
}
