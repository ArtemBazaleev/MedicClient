package com.example.medicapp.networking.data;

public class LogOutBody {
    private String token;

    public LogOutBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
