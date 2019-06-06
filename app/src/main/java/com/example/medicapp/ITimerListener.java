package com.example.medicapp;

public interface ITimerListener {

    void onTick(long mills);
    void onTimerFinished();

}
