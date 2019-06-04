package com.example.medicapp.networking.registration;

public class ConfirmBody {
    private String login;

    public ConfirmBody(String login, String code) {
        this.login = login;
        this.code = code;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;
}
