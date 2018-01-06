package com.example.testmodule.notification.views;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.notification.model.AppInfo;
import com.example.testmodule.notification.notifiutils.NotifyBlockManager;
import com.example.testmodule.notification.notifiutils.NotifyBlockManager.APP_TYPE;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotiBlockActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_block);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
        initData();
    }

    private void initData(){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                NotifyBlockManager.get(mContext);//this will cost much time
            }
        });
    }

    @OnClick(R.id.btn1)
    public void onClick1(){
        int uid = -1;
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            uid = appInfo.uid;
            ALog.Log("enabled: "+ NotifyBlockManager.areNotificationsEnabledForPackage(getPackageName(), uid));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn2)
    public void onClick2(){
        int uid = -1;
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(),
                    PackageManager.GET_META_DATA);
            uid = appInfo.uid;
            boolean blocked = NotifyBlockManager.areNotificationsEnabledForPackage(getPackageName(), uid);
            ALog.Log("enabled: "+ NotifyBlockManager.setNotificationsEnabledForPackage(getPackageName(),
                    uid, !blocked));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn3)
    public void onClick3(){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ALog.Log("/------------------------NotifyBlockManager.APP_TYPE.FLAG_ALL----------------------/");
                List<AppInfo> apps = NotifyBlockManager.get(mContext).getAppsInfo(APP_TYPE.FLAG_ALL);
                visitList(apps);
                ALog.Log("/------------------------NotifyBlockManager.APP_TYPE.FLAG_NO_SYSTEM----------------------/");
                apps = NotifyBlockManager.get(mContext).getAppsInfo(APP_TYPE.FLAG_NO_SYSTEM);
                visitList(apps);
                ALog.Log("/------------------------NotifyBlockManager.APP_TYPE.FLAG_WHITE_LIST----------------------/");
                apps = NotifyBlockManager.get(mContext).getAppsInfo(APP_TYPE.FLAG_WHITE_LIST);
                visitList(apps);
            }
        });
    }

    private void visitList(List<AppInfo> apps){
        for(AppInfo app : apps){
            ALog.Log("appName: "+app.appName);
            ALog.Log("packageName: "+app.packageName);
        }
    }

    int []buttonIDs={R.id.btn4};
    Class<?>[] classEs ={NotiAppActivity.class};

    @OnClick({R.id.btn4})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }

    @Override
    public void onDestroy(){
        NotifyBlockManager.get(getApplicationContext()).clear();
        super.onDestroy();
    }
}
