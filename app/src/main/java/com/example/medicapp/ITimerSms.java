package com.example.medicapp;

public interface ITimerSms {

    void startTimer(long mils);
    void stopTimer();
    void setTimerListener(ITimerListener listener);
    boolean isTicking();

}
