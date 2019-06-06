package com.example.medicapp.networking.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("growth")
    @Expose
    private double growth;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("sport")
    @Expose
    private Boolean sport;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("workType")
    @Expose
    private String workType;

    public Profile(Integer age, double growth, String name, String sex, Boolean sport, String surname, double weight, String workType) {
        this.age = age;
        this.growth = growth;
        this.name = name;
        this.sex = sex;
        this.sport = sport;
        this.surname = surname;
        this.weight = weight;
        this.workType = workType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public double getGrowth() {
        return growth;
    }

    public void setGrowth(double growth) {
        this.growth = growth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Boolean getSport() {
        return sport;
    }

    public void setSport(Boolean sport) {
        this.sport = sport;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }
}
