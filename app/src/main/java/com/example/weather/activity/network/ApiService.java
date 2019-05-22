package com.example.weather.activity.network;

import com.example.weather.activity.model.CurrentWeather;
import com.example.weather.activity.model.ServerResponse;

import java.util.Map;

public interface ApiService {
    void loadWeather(ResponseCallback<ServerResponse> responseCallback,String lat,String lon,String cnt,String appid);
    void loadCurrentWeather(ResponseCallback<CurrentWeather> responseCallback, String lat, String lon, String appid);
}
