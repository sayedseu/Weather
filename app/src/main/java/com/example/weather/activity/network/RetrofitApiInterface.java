package com.example.weather.activity.network;

import com.example.weather.activity.model.CurrentWeather;
import com.example.weather.activity.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RetrofitApiInterface {
    @GET("/data/2.5/find")
    Call<ServerResponse> getWeather(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("cnt") String cnt,
            @Query("appid") String appid
    );

    @GET("/data/2.5/weather")
    Call<CurrentWeather> getCurrentWeather(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String appid
    );
}
