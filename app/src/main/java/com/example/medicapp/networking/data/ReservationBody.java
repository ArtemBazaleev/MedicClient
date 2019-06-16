package com.example.medicapp.networking.data;

public class ReservationBody {
    private String date;
    private String time;
    private String name;
    private String surname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ReservationBody(String date, String time, String name, String surname) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.surname = surname;
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
