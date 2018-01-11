/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mt.androidtest_as.alog;

import android.app.Application;

import com.example.androidcommon.crashhandle.CrashManager;
import com.mt.androidtest_as.MainActivity;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.BuildConfig;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.ApplicationComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.components.DaggerApplicationComponent;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.di.modules.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * Android Main Application
 */
public class AndroidTest_AS_Application extends Application {

    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.initializeLeakDetection();
        //以下为应用注册未捕获异常处理接口
        CrashManager crashHandler = new CrashManager(this, MainActivity.class);
//        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }
}
