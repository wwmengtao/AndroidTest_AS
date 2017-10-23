package com.mt.androidtest_as.alog;

import android.app.Application;

import com.mt.myapplication.novicetutorial.dagger.AppComponent;
import com.mt.myapplication.novicetutorial.dagger.AppModule;
import com.mt.myapplication.novicetutorial.dagger.DaggerAppComponent;

/**
 * Created by mengtao1 on 2017/10/23.
 */

public class AndroidTest_AS_Application extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent=initDagger(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    protected AppComponent initDagger(AndroidTest_AS_Application application) {
        return DaggerAppComponent.builder()//如果编译时候提示DaggerAppComponent找不到，那么点击Build->"Make module 'app'"
                .appModule(new AppModule(application))
                .build();
    }
}
