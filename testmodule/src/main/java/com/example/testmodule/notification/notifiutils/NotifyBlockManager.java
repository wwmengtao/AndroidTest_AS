package com.example.testmodule.notification.notifiutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.testmodule.notification.appinfos.AppInfo;
import com.example.testmodule.notification.appinfos.AppInfoCom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 由于权限问题，只能模拟framework_o模块中的需要系统权限才能执行的NotifyBlockManager
 * Created by mengtao1 on 2018/1/4.
 */

public class NotifyBlockManager {
    private Context mContext = null;
    private volatile static NotifyBlockManager mNotifyBlockManager = null;
    private PackageManager mPackageManager = null;
    private List<AppInfo> mAppInfoList = null;

    public static NotifyBlockManager get(Context context){
        if(null == mNotifyBlockManager){
            mNotifyBlockManager = new NotifyBlockManager(context);
        }
        return mNotifyBlockManager;
    }

    private NotifyBlockManager(Context context){
        this.mContext = context.getApplicationContext();
        this.mPackageManager = mContext.getPackageManager();
        this.mAppInfoList = new ArrayList<>();
    }

    public enum APP_TYPE{
        FLAG_ALL,
        FLAG_SYSTEM,
        FLAG_NO_SYSTEM
    }

    /**
     *
     * @param type：用于区分当前应用的种类，例如FLAG_NO_SYSTEM可以排除系统应用
     * @return
     */
    public List<AppInfo> getAppsInfo(APP_TYPE type) {
        mAppInfoList.clear();
        //1.define values field
        AppInfo mAppInfo = null;
        String appName = null;
        String packageName = null;
        int uid = -1;
        Drawable appIcon = null;
        Intent launchIntent = null;
        List<PackageInfo> packs = mPackageManager.getInstalledPackages(0);
        int count = packs.size() - 1;
        for (int i = 0; i <= count; i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo itemInfo = p.applicationInfo;
            if ((itemInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                if(APP_TYPE.FLAG_NO_SYSTEM == type) {//filter system apps
                    continue;
                }
            }
            //2.generate AppInfo
            appName = p.applicationInfo.loadLabel(mPackageManager).toString().trim();
            packageName = p.packageName;
            uid = p.applicationInfo.uid;
            appIcon = p.applicationInfo.loadIcon(mPackageManager);
            launchIntent = mPackageManager.getLaunchIntentForPackage(packageName);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//FLAG_ACTIVITY_NEW_TASK一般配合FLAG_ACTIVITY_CLEAR_TOP使用
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mAppInfo = new AppInfo(appName, packageName, uid, appIcon, launchIntent);
            mAppInfoList.add(mAppInfo);
        }
        //3.sort the list
        AppInfoCom comparator = new AppInfoCom();
        Collections.sort(mAppInfoList, comparator);
        return mAppInfoList;
    }

    //simulate INotificationManager.setNotificationsEnabledForPackage
    public static boolean setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled) {
        return false;
    }

    //simulate INotificationManager.areNotificationsEnabledForPackage
    public static boolean areNotificationsEnabledForPackage(String pkg, int uid){
        return false;
    }
}
