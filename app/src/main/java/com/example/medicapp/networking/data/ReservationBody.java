package com.example.medicapp.networking.data;

public class ReservationBody {
    private String date;
    private String time;

    public ReservationBody(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDay() {
        return date;
    }

    public void setDay(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
