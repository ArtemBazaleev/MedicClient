package com.example.medicapp.model;

import com.example.medicapp.networking.data.ResponseProfile;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileModel {

    private double height;
    private double weight;
    private int age;
    private String name = "";
    private String surname = "";
    private boolean isMale;
    private boolean isLazyJob;
    private boolean isDoSport;
    private String imageURL;

    public ProfileModel() {
    }

    public ProfileModel(String json) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(json);
            name = jObj.getJSONObject("data").getJSONObject("patient").getString("name");
            surname = jObj.getJSONObject("data").getJSONObject("patient").getString("surname");
            isMale = jObj.getJSONObject("data").getJSONObject("patient").getString("sex").equals("male");
            isLazyJob = jObj.getJSONObject("data").getJSONObject("patient").getString("workType").equals("sedentary");
            height = jObj.getJSONObject("data").getJSONObject("patient").getDouble("growth");
            isDoSport = jObj.getJSONObject("data").getJSONObject("patient").getBoolean("sport");
            weight = jObj.getJSONObject("data").getJSONObject("patient").getDouble("weight");
            age = jObj.getJSONObject("data").getJSONObject("patient").getInt("age");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ProfileModel(ResponseProfile profile) {

        height = profile.getPatient().getGrowth();
        weight = profile.getPatient().getWeight();
        age = profile.getPatient().getAge();
        name = profile.getPatient().getName();
        surname = profile.getPatient().getSurname();
        isMale = profile.getPatient().getSex().equals("male");
        isLazyJob = !profile.getPatient().getWorkType().equals("active");
        isDoSport = profile.getPatient().getSport();
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

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

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public boolean isLazyJob() {
        return isLazyJob;
    }

    public void setLazyJob(boolean lazyJob) {
        isLazyJob = lazyJob;
    }

    public boolean isDoSport() {
        return isDoSport;
    }

    public void setDoSport(boolean doSport) {
        isDoSport = doSport;
    }
}
