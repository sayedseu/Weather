package com.example.weather.activity.network;

import com.example.weather.activity.model.CurrentWeather;
import com.example.weather.activity.model.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitHelper implements ApiService {
    private static final String TAG = "service";

    private RetrofitApiInterface apiInterface;

    public RetrofitHelper(RetrofitApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }


    @Override
    public void loadWeather(final ResponseCallback<ServerResponse> responseCallback, String lat, String lon, String cnt, String appid) {
        apiInterface.getWeather(lat, lon, cnt, appid).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getList() != null) {

                        responseCallback.onSuccess(response.body());

                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                responseCallback.onError(t);
            }
        });
    }

    @Override
    public void loadCurrentWeather(final ResponseCallback<CurrentWeather> responseCallback, String lat, String lon, String appid) {
        apiInterface.getCurrentWeather(lat, lon, appid).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        responseCallback.onSuccess(response.body());

                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                responseCallback.onError(t);
            }
        });
    }


}
