package com.example.testmodule.sysapps.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.example.testmodule.ALog;
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
    private static final String TAG = "SysAppsManager";
    private volatile static SysAppsManager mSysAppsManager = null;
    private Context mContext = null;
    private PackageManager mPackageManager = null;
    private List<AppInfo> mAppInfosList = null;
    private List<OnAppChangedListener> mAppChangedListeners = null;

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
        this.mAppChangedListeners = new ArrayList<>();
        this.mAppInfosList = new ArrayList<>();
        getAppInfoList();
    }

    public interface OnAppChangedListener{
        void onAppChanged();
    }

    public synchronized void addOnAppChangedListener(OnAppChangedListener listener){
        if(null != mAppChangedListeners)mAppChangedListeners.add(listener);
    }

    public synchronized void removeOnAppChangedListener(OnAppChangedListener listener){
        if(null != mAppChangedListeners && mAppChangedListeners.size() > 0){
            mAppChangedListeners.remove(listener);
        }
    }

    @SuppressLint("WrongConstant")
    public synchronized List<AppInfo> getAppInfoList(){
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
        if(null != mAppChangedListeners)mAppChangedListeners.clear();
        if(null != mAppInfosList)mAppInfosList.clear();
        mSysAppsManager = null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public synchronized void onPackageInstalled(String packageName) {
        if(null == mAppInfosList || mAppInfosList.size() == 0)return;
        //1.判断该应用是否已经存在
        AppInfo mAppInfo;
        for(int i = 0; i < mAppInfosList.size(); i++){
            mAppInfo = mAppInfosList.get(i);
            if(mAppInfo.packageName.equals(packageName)){
                return;
            }
        }//end for
        //2.获取该应用对应的AppInfo
        Intent it = mPackageManager.getLaunchIntentForPackage(packageName);
        ResolveInfo info = mPackageManager.resolveActivity(it,PackageManager.GET_ACTIVITIES);
        mAppInfosList.add(getAppInfo(info));
        AppInfoCom comparator = new AppInfoCom();
        Collections.sort(mAppInfosList, comparator);
        //3.回调监听，反应应用的安装、卸载情况
        trigerCallBack();
    }

    @Override
    public synchronized void onPackageUnInstalled(String packageName) {
        if(null == mAppInfosList || mAppInfosList.size() == 0)return;
        //1.获取该应用对应的AppInfo
        AppInfo mAppInfo;
        for(int i = 0; i < mAppInfosList.size(); i++){
            mAppInfo = mAppInfosList.get(i);
            ALog.Log(mAppInfo.packageName+" VS: "+packageName);
            if(mAppInfo.packageName.equals(packageName)){
                mAppInfosList.remove(i);
                break;
            }
        }//end for
        //2.回调监听，反应应用的安装、卸载情况
        trigerCallBack();
    }

    private void trigerCallBack(){
        if(null != mAppChangedListeners && mAppChangedListeners.size() > 0) {
            for (OnAppChangedListener listener : mAppChangedListeners) {
                listener.onAppChanged();
            }
        }
    }
}
