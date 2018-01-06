package com.example.testmodule.notification.notifiutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import com.example.testmodule.notification.model.AppInfo;
import com.example.testmodule.notification.model.AppInfoCom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 由于权限问题，只能模拟framework_o模块中的需要系统权限才能执行的NotifyBlockManager
 * Created by mengtao1 on 2018/1/4.
 */

public class NotifyBlockManager {
    private Context mContext = null;
    private volatile static NotifyBlockManager mNotifyBlockManager = null;
    private PackageManager mPackageManager = null;
    private List<PackageInfo> allPackageInfos = null;
    private ConcurrentHashMap<APP_TYPE, List<AppInfo>> mAppInfoListCol = null;

    public static NotifyBlockManager get(Context context){
        if(null == mNotifyBlockManager){
            synchronized (NotifyBlockManager.class){
                if(null == mNotifyBlockManager) {
                    mNotifyBlockManager = new NotifyBlockManager(context);
                }
            }
        }
        return mNotifyBlockManager;
    }

    private NotifyBlockManager(Context context){
        this.mContext = context.getApplicationContext();
        this.mPackageManager = mContext.getPackageManager();
        this.allPackageInfos = mPackageManager.getInstalledPackages(0);
        this.mAppInfoListCol = new ConcurrentHashMap<>();
        initAppInfoListCol();
    }

    private void initAppInfoListCol(){
        getAppsInfo(APP_TYPE.FLAG_ALL);
        getAppsInfo(APP_TYPE.FLAG_NO_SYSTEM);
        getAppsInfo(APP_TYPE.FLAG_WHITE_LIST);
    }

    private static final String[] AppsWhiteList = {
            "com.google.android.music",
            "com.google.android.talk",
            "com.google.android.apps.maps",
            "com.google.android.feedback",
            "com.google.android.apps.messaging",
            "com.google.android.apps.photos",
    };


    public enum APP_TYPE{
        FLAG_ALL,
        FLAG_NO_SYSTEM,//filter system app
        FLAG_WHITE_LIST,
        FLAG_WHITE_LIST_NOTI_BLOCKED,
        FLAG_WHITE_LIST_NOTI_UNBLOCKED
    }

    private List<AppInfo> getWhiteListBlockAppsInfo(APP_TYPE type){
        List<AppInfo> mAppInfoList = mAppInfoListCol.get(APP_TYPE.FLAG_WHITE_LIST);
        boolean blocked = (APP_TYPE.FLAG_WHITE_LIST_NOTI_BLOCKED == type) ? true : false;
        List<AppInfo> mBlockedAppInfoList = null;
        for(AppInfo ai : mAppInfoList){
            if(ai.notiBlocked == blocked){
                if(null == mBlockedAppInfoList)mBlockedAppInfoList = new ArrayList<>();
                mBlockedAppInfoList.add(ai);
            }
        }
        return mBlockedAppInfoList;
    }

    /**
     *
     * @param type：用于区分当前应用的种类，例如FLAG_NO_SYSTEM可以排除系统应用
     * @return
     */
    public synchronized List<AppInfo> getAppsInfo(APP_TYPE type) {
        if(APP_TYPE.FLAG_WHITE_LIST_NOTI_BLOCKED == type || APP_TYPE.FLAG_WHITE_LIST_NOTI_UNBLOCKED == type){
            return getWhiteListBlockAppsInfo(type);
        }
        List<AppInfo> mAppInfoList = mAppInfoListCol.get(type);
        if(null != mAppInfoList){
            return mAppInfoList;
        }
        mAppInfoList = new ArrayList<>();
        //1.define values field
        AppInfo mAppInfo = null;
        String appName = null;
        String packageName = null;
        int uid = -1;
        Drawable appIcon = null;
        Intent launchIntent = null;

        for (PackageInfo p : allPackageInfos) {
            ApplicationInfo itemInfo = p.applicationInfo;
            if (APP_TYPE.FLAG_NO_SYSTEM == type &&
                                (itemInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {//filter system apps
                continue;
            }else if(APP_TYPE.FLAG_WHITE_LIST == type && !isWhiteListApp(p.packageName)){
                continue;
            }
            //2.generate AppInfo
            appName = p.applicationInfo.loadLabel(mPackageManager).toString().trim();
            packageName = p.packageName;
            //filter apps that appname equals packagename
            if(null !=appName && null != packageName && appName.equals(packageName)){
                continue;
            }
            uid = p.applicationInfo.uid;
            appIcon = p.applicationInfo.loadIcon(mPackageManager);
            launchIntent = mPackageManager.getLaunchIntentForPackage(packageName);
            if(null != launchIntent) {
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//FLAG_ACTIVITY_NEW_TASK一般配合FLAG_ACTIVITY_CLEAR_TOP使用
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            mAppInfo = new AppInfo(appName, packageName, uid, appIcon, launchIntent);
            mAppInfo.setNotiBlocked(true);//We default to
            mAppInfoList.add(mAppInfo);
        }
        //3.sort the list
        AppInfoCom comparator = new AppInfoCom();
        Collections.sort(mAppInfoList, comparator);
        //4.restore mAppInfoList
        mAppInfoListCol.put(type, mAppInfoList);
        return mAppInfoList;
    }

    private boolean isWhiteListApp(String packageName){
        if(null == packageName)return false;
        for(String str : AppsWhiteList){
            if(str.equals(packageName)){
                return true;
            }
        }
        return false;
    }

    private static final String CalendarPackageName = "com.google.android.calendar";
    public Pair<String, Drawable> getCalendarInfo(){
        Pair<String, Drawable> mPair = null;
        for (PackageInfo p : allPackageInfos) {
            if(p.packageName.equals(CalendarPackageName)){
                String appName = p.applicationInfo.loadLabel(mPackageManager).toString().trim();
                Drawable icon = p.applicationInfo.loadIcon(mPackageManager);
                mPair = new Pair(appName, icon);
                break;
            }
        }
        return mPair;
    }


    //simulate INotificationManager.setNotificationsEnabledForPackage
    public static boolean setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled) {
        return false;
    }

    //simulate INotificationManager.areNotificationsEnabledForPackage
    public static boolean areNotificationsEnabledForPackage(String pkg, int uid){
        return false;
    }

    public void clear(){
        if(null != allPackageInfos)allPackageInfos.clear();
        if(null != mAppInfoListCol)mAppInfoListCol.clear();
        mNotifyBlockManager = null;
    }
}
