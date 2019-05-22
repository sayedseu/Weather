package com.example.weather.activity.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppNetMoudle {

    private String BASEURL;

    public AppNetMoudle(String BASEURL) {
        this.BASEURL = BASEURL;
    }


    @Singleton
    @Provides
    Gson getGson() {
        Gson gson = new GsonBuilder()
                .setLenient().create();
        return gson;
    }

    @Singleton
    @Provides
    Retrofit getRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASEURL)
                .build();
        return retrofit;
    }
}
