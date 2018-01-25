package com.example.testmodule.sysapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.example.testmodule.notification.model.AppInfo;
import com.example.testmodule.notification.model.AppInfoCom;
import com.example.testmodule.receivers.PackageInstallReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by mengtao1 on 2018/1/24.
 */

public class SysAppsManager implements PackageInstallReceiver.OnPackageAddRemoveListener{
    private volatile static SysAppsManager mSysAppsManager = null;
    private Context mContext = null;
    private PackageManager mPackageManager = null;
    private List<AppInfo> mAppInfosList = null;

    public static SysAppsManager get(Context context){
        if(null == mSysAppsManager){
            synchronized (SysAppsManager.class){
                if(null == mSysAppsManager) {
                    mSysAppsManager = new SysAppsManager(context);
                }
            }
        }
        return mSysAppsManager;
    }

    private SysAppsManager(Context context) {
        this.mContext = context.getApplicationContext();
        this.mPackageManager = context.getPackageManager();
        this.mAppInfosList = new ArrayList<>();
        getAppInfoList();
    }

    @SuppressLint("WrongConstant")
    private synchronized List<AppInfo> getAppInfoList(){
        if(null != mAppInfosList && mAppInfosList.size() > 0){
            return mAppInfosList;
        }
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(it, PackageManager.GET_ACTIVITIES);
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(mPackageManager));
        for(ResolveInfo info : resolveInfos) {
            mAppInfosList.add(getAppInfo(info));
        }
        //排序
        AppInfoCom comparator = new AppInfoCom();
        Collections.sort(mAppInfosList, comparator);
        return mAppInfosList;
    }

    @SuppressLint("WrongConstant")
    private AppInfo getAppInfo(ResolveInfo info){
        AppInfo mAppInfo = new AppInfo();
        String appName = info.loadLabel(mPackageManager).toString();
        String packageName = info.activityInfo.packageName;
        String className = info.activityInfo.name;
        Drawable icon = null;
        String sourceDir = null;
        try {
            icon = info.loadIcon(mPackageManager);
            sourceDir = mPackageManager.getApplicationInfo(info.activityInfo.packageName, PackageManager.GET_ACTIVITIES).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mAppInfo.icon = icon;
        mAppInfo.appName = appName;
        mAppInfo.packageName = packageName;
        mAppInfo.className = className;
        mAppInfo.sourceDir = sourceDir;
        return mAppInfo;
    }

    public void clear(){
        if(null == mSysAppsManager)return;
        if(null != mAppInfosList)mAppInfosList.clear();
        mSysAppsManager = null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public synchronized void onPackageInstalled(String packageName) {
        if(null == mAppInfosList || mAppInfosList.size() == 0)return;
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = mPackageManager.resolveActivity(it,PackageManager.GET_ACTIVITIES);
        mAppInfosList.add(getAppInfo(info));
    }

    @Override
    public synchronized void onPackageUnInstalled(String packageName) {
        if(null == mAppInfosList || mAppInfosList.size() == 0)return;
        AppInfo mAppInfo;
        for(int i = 0; i < mAppInfosList.size(); i++){
            mAppInfo = mAppInfosList.get(i);
            if(mAppInfo.packageName.equals(packageName)){
                mAppInfosList.remove(i);
                return;
            }
        }
    }
}
