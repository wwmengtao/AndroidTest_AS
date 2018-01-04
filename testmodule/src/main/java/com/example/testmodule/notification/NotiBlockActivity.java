package com.example.testmodule.notification;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.framework_o.notification.NotifyBlockManager;
import com.example.testmodule.ALog;
import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotiBlockActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_block);
        mUnbinder = ButterKnife.bind(this);
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
}
