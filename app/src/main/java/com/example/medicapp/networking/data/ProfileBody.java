package com.example.medicapp.networking.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileBody {
    public ProfileBody(Profile profile) {
        this.profile = profile;
    }

    @SerializedName("profile")
    @Expose
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
