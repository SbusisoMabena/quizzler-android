package com.example.quizzlerandroid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("results")
    private List<Quiz> data;

    public List<Quiz> getData() {
        return data;
    }

    public void setData(List<Quiz> data) {
        this.data = data;
    }
}
