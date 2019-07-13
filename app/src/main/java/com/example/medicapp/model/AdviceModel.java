package com.example.medicapp.model;

import com.example.medicapp.networking.response.advice.Advice;

public class AdviceModel {

    public static final int MODE_TXT = 0;
    public static final int MODE_IMAGES = 1;
    public static final int MODE_VIDEO = 2;

    private int mMode;

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    private Advice advice;

    public AdviceModel(Advice advice, int mode) {
        this.advice = advice;
        this.mMode = mode;
    }

    public int getmMode() {
        return mMode;
    }

    public void setmMode(int mMode) {
        this.mMode = mMode;
    }
}
