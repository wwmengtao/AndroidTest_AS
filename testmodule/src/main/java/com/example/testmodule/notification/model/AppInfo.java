package com.example.testmodule.notification.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by mengtao1 on 2018/1/2.
 */

public class AppInfo {
    public String appName;
    public String packageName;
    public int uid;
    public Drawable icon;
    public Intent launchIntent = null;
    //
    public boolean notiBlocked;


    public AppInfo(String appName, String packageName, int uid, Drawable icon, Intent launchIntent) {
        this.appName = appName;
        this.packageName = packageName;
        this.uid = uid;
        this.icon = icon;
        this.launchIntent = launchIntent;
    }

    public void setNotiBlocked(boolean notiBlocked){
        this.notiBlocked = notiBlocked;
    }

}
