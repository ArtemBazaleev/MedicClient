package com.example.medicapp.networking.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Patient {@SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("created")
    @Expose
    private long created;
    @SerializedName("growth")
    @Expose
    private float growth;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;
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
    private float weight;
    @SerializedName("workType")
    @Expose
    private String workType;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public float getGrowth() {
        return growth;
    }

    public void setGrowth(float growth) {
        this.growth = growth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }
}
