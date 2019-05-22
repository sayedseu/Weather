package com.example.weather.activity.di;

import com.example.weather.activity.notification.MyService;

import dagger.Component;

@PerService
@Component(dependencies = AppComponent.class , modules = ServiceModule.class)
public interface ServiceComponent {

    void inject(MyService myService);


    @Component.Builder
    interface Builder{

        Builder appComponent(AppComponent appComponent);
        ServiceComponent build();
    }
}
