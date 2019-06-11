package com.example.medicapp.networking.response.date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hours {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("times")
    @Expose
    private List<Time> times = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }
}
