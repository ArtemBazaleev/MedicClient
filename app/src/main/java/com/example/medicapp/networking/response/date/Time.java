package com.example.medicapp.networking.response.date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Time {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("reserved")
    @Expose
    private Boolean reserved;
    @SerializedName("by")
    @Expose
    private String by;
    @SerializedName("created")
    @Expose
    private Integer created;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}
