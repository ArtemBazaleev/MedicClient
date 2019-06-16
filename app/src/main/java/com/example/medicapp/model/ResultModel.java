package com.example.medicapp.model;

public class ResultModel{
    public static final int TYPE_BACKBONE = 0;
    public static final int TYPE_CONCLUSION = 2;
    public static final int TYPE_OTHER = 1;

    private String url;
    private String desc;
    private String created;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ResultModel() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ResultModel(String url, String desc, String created, int type) {
        this.url = url;
        this.desc = desc;
        this.created = created;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
