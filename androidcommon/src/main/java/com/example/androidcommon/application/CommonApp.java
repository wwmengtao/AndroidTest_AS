package com.example.androidcommon.application;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.androidcommon.alog.ALog;
import com.example.androidcommon.crashhandle.CrashManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Android Application class. Used for accessing singletons.
 */
public abstract class CommonApp extends Application {
    private static final String TAG = "BasicApp_";
    private AppExecutors mAppExecutors;
    private AtomicBoolean isBackground = new AtomicBoolean(true);

    /**
     * CommonApp是Application类型的，只有它的onCreate执行完毕才能轮到后续的组件执行onCreate等，因此
     * Application.onCreate中不能有太多的耗时操作
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ALog.Log(TAG+"onCreate begin");
        //1、初始化全局工作线程池
        this.mAppExecutors = new AppExecutors();
        //2、监听应用的前台/后台变化
        listenForForeground();//监听应用是否已到前台
        listenForScreenTurningState();//监听屏幕亮灭状态
        //3、捕获应用异常崩溃
        ALog.Log(TAG+"onCreate end");
    }

    /**
     *initCrashManager：初始化应用崩溃处理器
     * @param activityClass：应用崩溃后重新开启应用后进入的Activity
     */
    protected void initCrashManager(Class<?> activityClass){
        CrashManager crashHandler = new CrashManager(this, activityClass);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    public void onLowMemory() {
        super.onLowMemory();
        ALog.Log(TAG+"onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {//判断应用是否切换至后台运行
            ALog.Log(TAG+"onTrimMemory_TRIM_MEMORY_UI_HIDDEN");
            isBackground.set(true);
            notifyBackground();
        }
    }

    protected void listenForForeground() {
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
                if (isBackground()) {
                    isBackground.set(false);
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

    protected void listenForScreenTurningState() {
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action){
                    case Intent.ACTION_SCREEN_ON:
                        notifyScreenOn();
                        break;
                    case Intent.ACTION_SCREEN_OFF:
                        notifyScreenOff();
                        break;
                }

            }
        }, screenStateFilter);
    }

    protected void notifyForeground() {
        // This is where you can notify listeners, handle session tracking, etc
        ALog.Log(TAG+"notifyForeground");
        //AppService.isInstanceCreated()可以保证每次应用show到前台的时候，AppService能存在且正在运行
    }

    protected void notifyBackground() {
        // This is where you can notify listeners, handle session tracking, etc
        ALog.Log(TAG+"notifyBackground");
    }

    public boolean isBackground() {
        return isBackground.get();
    }

    protected void notifyScreenOn() {
        // This is where you can notify listeners, handle session tracking, etc
        ALog.Log(TAG + "notifyScreenOn");
    }

    private void notifyScreenOff() {
        // This is where you can notify listeners, handle session tracking, etc
        ALog.Log(TAG+"notifyScreenOff");
    }

    public AppExecutors getAppExecutors(){
        return this.mAppExecutors;
    }

}
