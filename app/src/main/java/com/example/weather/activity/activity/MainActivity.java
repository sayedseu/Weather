package com.example.weather.activity.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.weather.R;
import com.example.weather.activity.MyApp;
import com.example.weather.activity.adapter.RecyclerViewAdapter;
import com.example.weather.activity.di.ActivityComponent;
import com.example.weather.activity.di.DaggerActivityComponent;
import com.example.weather.activity.gps.AppConstant;
import com.example.weather.activity.gps.GpsUtils;
import com.example.weather.activity.model.ServerResponse;
import com.example.weather.activity.network.ResponseCallback;
import com.example.weather.activity.network.RetrofitHelper;
import com.example.weather.activity.notification.MyService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String API_KEY = "ecde07e52d44aac24abefb8832f3f307";
    private static final int MY_REQUEST_CODE = 1;
    private static Boolean flag = true;
    @BindView(R.id.recID)
    RecyclerView recyclerView;
    @Inject
    RetrofitHelper retrofitHelper;
    @Inject
    RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ActivityComponent component;
    private FusedLocationProviderClient client;
    private Boolean mLocationPermissionGranted = false;
    private Boolean isGPS = false;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        component = DaggerActivityComponent.builder()
                .context(this)
                .appComponent(((MyApp) getApplication()).getAppComponent())
                .build();
        component.inject(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        client = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();
        createLocationRequest();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "onLocationResult: " + location);
                    if (location != null) {
                        if (client != null) {
                            client.removeLocationUpdates(locationCallback);
                            requestWeatherData(location);
                            break;
                        }
                    }
                }
            }
        };

    }


    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: ");

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            checkGPS();
            Log.d(TAG, "getLocationPermission: permision granted");
        } else {
            Log.d(TAG, "getLocationPermission: permision not granted");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_CODE);
        }
    }

    protected void createLocationRequest() {
        Log.d(TAG, "createLocationRequest: ");
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                checkGPS();
            }
        }
    }

    private void getLocation() {

        if (isGPS == false) {
            return;
        }
        try {
            client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        client.requestLocationUpdates(locationRequest, locationCallback, null);
                        Log.d(TAG, "onSuccess: " + location);
                    } else {
                        requestWeatherData(location);
                        Log.d(TAG, "onSuccess: " + location);
                    }

                }
            });

        } catch (SecurityException e) {

        }
    }

    private void requestWeatherData(Location location) {

        Log.d(TAG, "requestWeatherData: ");

        if (location == null) {
            Log.d(TAG, "requestWeatherData: null");
            return;
        }

        retrofitHelper.loadWeather(new ResponseCallback<ServerResponse>() {
                                       @Override
                                       public void onSuccess(ServerResponse data) {
                                           try {
                                               Log.d(TAG, "onSuccess: " + data.getList().size());
                                               recyclerViewAdapter.setData(data.getList());
                                               recyclerView.setAdapter(recyclerViewAdapter);
                                           } catch (Exception e) {

                                           }

                                       }

                                       @Override
                                       public void onError(Throwable throwable) {
                                           Log.e(TAG, "onError: " + throwable);
                                       }
                                   }, String.valueOf(location.getLatitude())
                , String.valueOf(location.getLongitude())
                , String.valueOf(10)
                , API_KEY);
        startService(location.getLatitude(), location.getLongitude());
    }

    private void checkGPS() {

        if (mLocationPermissionGranted) {

            new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    isGPS = isGPSEnable;
                }
            });

        }
        if (isGPS) {
            Log.d(TAG, "checkGPS: ");
            getLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstant.GPS_REQUEST) {
                isGPS = true;
                Log.d(TAG, "onActivityResult: " + isGPS);
                getLocation();
            }
        }
    }

    private void startService(double lat, double lon) {

        if (flag) {
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(MyService.LAT_KEY, lat);
            intent.putExtra(MyService.LON_KEY, lon);
            startService(intent);
            flag = false;
        }


    }
}
