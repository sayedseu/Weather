package com.example.weather.activity.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.example.weather.R;
import com.example.weather.activity.MyApp;
import com.example.weather.activity.activity.DetailsActivity;
import com.example.weather.activity.di.DaggerServiceComponent;
import com.example.weather.activity.di.ServiceComponent;
import com.example.weather.activity.model.CurrentWeather;
import com.example.weather.activity.network.ResponseCallback;
import com.example.weather.activity.network.RetrofitHelper;

import java.text.DecimalFormat;

import javax.inject.Inject;

public class MyService extends IntentService {
    public static final String LAT_KEY = "LAT KEY";
    public static final String LON_KEY = "LON_KEY";
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "service";
    private static final String API_KEY = "ecde07e52d44aac24abefb8832f3f307";
    private static final String CHANEL_ID = "CHANEL ID";
    private static final String CHANEL_NAME = "CHANEL NAME";
    private static final String BASE_URL = "http://openweathermap.org/img/w/";
    @Inject
    RetrofitHelper retrofitHelper;
    private ServiceComponent serviceComponent;


    public MyService(String name) {
        super(name);
    }

    public MyService() {
        super("MyService");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceComponent = DaggerServiceComponent.builder()
                .appComponent(((MyApp) getApplication()).getAppComponent())
                .build();
        serviceComponent.inject(this);

    }

    @Override
    protected void onHandleIntent(Intent intent) {


        double lat = intent.getExtras().getDouble(LAT_KEY);
        double lon = intent.getExtras().getDouble(LON_KEY);

        retrofitHelper.loadCurrentWeather(new ResponseCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {
                if (data != null) {
                    createNotification(data);
                }

            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, String.valueOf(lat), String.valueOf(lon), API_KEY);


    }

    private void createNotification(CurrentWeather data) {

        NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.cityNameID, data.getName());
        remoteViews.setTextViewText(R.id.conditionID, data.getWeather().get(0).getMain());
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        String temp = decimalFormat.format(data.getMain().getTemp() - 273.15);
        remoteViews.setTextViewText(R.id.tempID, temp + " \u2103");

        Intent resultIntent = new Intent(this, DetailsActivity.class);
        resultIntent.putExtra(DetailsActivity.KEY, data);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID
                    , CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }


        Notification customNotification = new NotificationCompat.Builder(this, CHANEL_ID)
                .setSmallIcon(R.drawable.storm)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, customNotification);

        NotificationTarget notificationTarget = new NotificationTarget(this, R.id.iconID,
                remoteViews, customNotification, NOTIFICATION_ID);

        Glide.with(this)
                .asBitmap()
                .load(BASE_URL + data.getWeather().get(0).getIcon() + ".png")
                .into(notificationTarget);


    }
}
