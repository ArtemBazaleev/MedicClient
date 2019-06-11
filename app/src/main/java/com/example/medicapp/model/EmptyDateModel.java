package com.example.medicapp.model;

public class EmptyDateModel {
    private boolean isSelected =false;

    private String date ="";

    private String time = "";

    public EmptyDateModel() {
    }

    public EmptyDateModel(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
