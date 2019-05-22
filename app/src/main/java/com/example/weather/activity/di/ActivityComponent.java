package com.example.weather.activity.di;

import android.content.Context;

import com.example.weather.activity.activity.MainActivity;

import dagger.BindsInstance;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityMoudle.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        Builder appComponent(AppComponent appComponent);

        ActivityComponent build();
    }
}
