package com.example.medicapp.model;

public class ProfileModel {

    private Double height;
    private Double weight;
    private int age;
    private String name;
    private String surname;
    private boolean isMale;
    private boolean isLazyJob;
    private boolean isDoSport;
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
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
