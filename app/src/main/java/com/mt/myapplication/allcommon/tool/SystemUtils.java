package com.mt.myapplication.allcommon.tool;

import android.app.ActivityManager;
import android.content.Context;

import com.mt.androidtest_as.alog.ALog;

import java.util.List;

public class SystemUtils {
    public static final String CURRENT_APP_PACKAGENAME = "com.mt.androidtest_as";
    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        String processInfo = null;
        for(int i = 0; i < processInfos.size(); i++){
            processInfo = processInfos.get(i).processName;
            ALog.Log("processInfo: "+processInfo);
            if(processInfo.equals(packageName)){
                return true;
            }
        }
        return false;
    }

}
