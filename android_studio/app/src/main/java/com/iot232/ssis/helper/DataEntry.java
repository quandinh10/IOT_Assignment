package com.iot232.ssis.helper;

import com.google.gson.annotations.SerializedName;

public class DataEntry {
    @SerializedName("value")
    private String value;

    @SerializedName("created_at")
    private String createdAt;


    // Getters and setters
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}