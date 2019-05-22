package com.example.weather.activity.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class List implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("main")
    private Main main;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("weather")
    private java.util.List<Weather> weather = null;

    public List() {
    }

    public List(String name, Coord coord, Main main, Wind wind, java.util.List<Weather> weather) {
        super();
        this.name = name;
        this.coord = coord;
        this.main = main;
        this.wind = wind;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }


    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

}
