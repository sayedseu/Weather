package com.example.weather.activity.di;

import android.content.Context;

import com.example.weather.activity.adapter.RecyclerViewAdapter;
import com.example.weather.activity.network.RetrofitApiInterface;
import com.example.weather.activity.network.RetrofitHelper;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ServiceModule {

    @PerService
    @Provides
    public RetrofitApiInterface getRetrofitApiInterface(Retrofit retrofit) {
        return retrofit.create(RetrofitApiInterface.class);
    }

    @PerService
    @Provides
    public RetrofitHelper getRetrofitHelper(RetrofitApiInterface apiInterface) {
        return new RetrofitHelper(apiInterface);
    }


}
