/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.testmodule.application;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.androidcommon.crashhandle.CrashManager;
import com.example.testmodule.ALog;
import com.example.testmodule.MainActivity;
import com.example.testmodule.services.AppService;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp extends Application {
    private static final String TAG = "BasicApp_";
    private Intent startServiceIntent = null;
    private AppExecutors mAppExecutors;
    private boolean isBackground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        ALog.Log(TAG+"onCreate");
        mAppExecutors = new AppExecutors();
        startServiceIntent = AppService.getLaunchIntent(this);
        if(!AppService.isInstanceCreated())startService(startServiceIntent);
        listenForForeground();//监听应用是否已到前台
        listenForScreenTurningState();//监听屏幕亮灭状态
        //用于捕获应用异常崩溃
        CrashManager crashHandler = new CrashManager(this, MainActivity.class);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    public void onLowMemory() {
        super.onLowMemory();
        ALog.Log(TAG+"onLowMemory");
    }

    private void listenForForeground() {
        //可以通过activity.getClass().getSimpleName()来监听特定的Activity生命周期
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ALog.Log(TAG+"onActivityCreated: "+activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ALog.Log(TAG+"onActivityStarted: "+activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ALog.Log(TAG+"onActivityResumed: "+activity.getClass().getSimpleName());
                if (isBackground) {
                    isBackground = false;
                    notifyForeground();//判断应用是否已经到了前台，任意Activity执行到onResume就会触发
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                ALog.Log(TAG+"onActivityPaused: "+activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ALog.Log(TAG+"onActivityStopped: "+activity.getClass().getSimpleName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                ALog.Log(TAG+"onActivitySaveInstanceState: "+activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ALog.Log(TAG+"onActivityDestroyed: "+activity.getClass().getSimpleName());
            }

        });
    }

    private void listenForScreenTurningState() {
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action){
                    case Intent.ACTION_SCREEN_ON:
                        isBackground = false;
                        notifyForeground();
                        break;
                    case Intent.ACTION_SCREEN_OFF:
                        isBackground = true;
                        notifyBackground();
                        break;
                }

            }
        }, screenStateFilter);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {//判断应用是否切换至后台运行
            ALog.Log(TAG+"onTrimMemory_TRIM_MEMORY_UI_HIDDEN");
            isBackground = true;
            notifyBackground();
        }

    }

    private void notifyForeground() {
        // This is where you can notify listeners, handle session tracking, etc
        ALog.Log(TAG+"notifyForeground");
        //AppService.isInstanceCreated()可以保证每次应用show到前台的时候，AppService能存在且正在运行
        if(!AppService.isInstanceCreated())startService(startServiceIntent);
    }

    private void notifyBackground() {
        // This is where you can notify listeners, handle session tracking, etc
        ALog.Log(TAG+"notifyBackground");
    }

    public boolean isBackground() {
        return isBackground;
    }

    public AppExecutors getAppExecutors(){
        return this.mAppExecutors;
    }

}
