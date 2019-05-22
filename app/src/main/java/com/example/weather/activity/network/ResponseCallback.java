package com.example.weather.activity.network;


public interface ResponseCallback<T> {
    void onSuccess(T data);

    void onError(Throwable throwable);
}
