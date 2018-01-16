package com.example.testmodule.services;

import android.app.ActivityManager;
import android.content.Context;

import com.example.testmodule.ALog;

import java.util.List;

/**
 * Created by mengtao1 on 2018/1/16.
 */

public class ServiceTools {
    private static String LOG_TAG = ServiceTools.class.getName();

    /**
     * 判断某Service是否正在运行，在同一个进程内判断无须此方法
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass){
        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            ALog.Log(String.format("Service:%s", runningServiceInfo.service.getClassName()));
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())){
                return true;
            }
        }
        return false;
    }
}