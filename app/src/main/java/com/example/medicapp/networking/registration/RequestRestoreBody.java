package com.example.medicapp.networking.registration;

public class RequestRestoreBody {
    private String login;

    public RequestRestoreBody(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
