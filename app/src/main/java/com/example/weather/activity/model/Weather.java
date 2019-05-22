package com.example.weather.activity.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Weather implements Serializable {

    @SerializedName("main")
    private String main;
    @SerializedName("icon")
    private String icon;

    public Weather() {
    }

    public Weather(String main, String icon) {
        super();
        this.main = main;
        this.icon = icon;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
