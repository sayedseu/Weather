package com.example.weather.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.weather.R;
import com.example.weather.activity.model.CurrentWeather;
import com.example.weather.activity.model.Main;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String KEY = "com.example.weather";


    @BindView(R.id.windID)
    TextView windTV;
    @BindView(R.id.pressure)
    TextView pressureTV;
    @BindView(R.id.humidityID)
    TextView humidityTV;
    @BindView(R.id.max_tempID)
    TextView man_temTV;
    @BindView(R.id.min_tempID)
    TextView min_tempTV;

    private GoogleMap mMap;
    private CurrentWeather data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getList();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    private void getList() {
        Intent intent = getIntent();
        if (intent != null) {
            data = (CurrentWeather) intent.getSerializableExtra(KEY);
            if (data != null) {
                bindView(data);
            }
        }

    }

    private void bindView(CurrentWeather list) {

        try {
            Main main = list.getMain();
            Double min_temp = main.getTempMin() - 273.15;
            Double max_temp = main.getTempMax() - 273.15;
            windTV.setText(list.getWind().getSpeed() + " m/s");
            pressureTV.setText(main.getPressure() + " hpa");
            humidityTV.setText(main.getHumidity() + " %");
            man_temTV.setText(new DecimalFormat("##.##").format(max_temp) + " \u2103");
            min_tempTV.setText(new DecimalFormat("##.##").format(min_temp) + " \u2103");
        } catch (Exception e) {

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (data != null) {
            LatLng sydney = new LatLng(data.getCoord().getLat(), data.getCoord().getLon());
            mMap.addMarker(new MarkerOptions().position(sydney).title(data.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }


    }
}
