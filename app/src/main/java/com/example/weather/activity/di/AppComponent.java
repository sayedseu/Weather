package com.example.weather.activity.di;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppsMoudle.class, AppNetMoudle.class})
public interface AppComponent {

    Retrofit getRetrofit();
}
