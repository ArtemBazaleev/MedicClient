
package com.example.medicapp.networking.response.results;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("conclusion")
    @Expose
    private String conclusion;
    @SerializedName("backbone")
    @Expose
    private List<String> backbone = null;
    @SerializedName("other")
    @Expose
    private List<Other> other = null;
    @SerializedName("created")
    @Expose
    private Integer created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public List<String> getBackbone() {
        return backbone;
    }

    public void setBackbone(List<String> backbone) {
        this.backbone = backbone;
    }

    public List<Other> getOther() {
        return other;
    }

    public void setOther(List<Other> other) {
        this.other = other;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

}
