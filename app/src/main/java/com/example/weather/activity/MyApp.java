package com.example.weather.activity;

import android.app.Application;

import com.example.weather.activity.di.AppComponent;
import com.example.weather.activity.di.AppNetMoudle;
import com.example.weather.activity.di.AppsMoudle;
import com.example.weather.activity.di.DaggerAppComponent;

public class MyApp extends Application {


    private static final String BASE_URL = "https://api.openweathermap.org/";
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appsMoudle(new AppsMoudle(this))
                .appNetMoudle(new AppNetMoudle(BASE_URL))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
