package com.example.testmodule.notification.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by mengtao1 on 2018/1/2.
 */

public class AppInfo {
    public String appName = null;
    public String packageName = null;
    public int uid = -1;
    public Drawable icon = null;
    public Intent launchIntent = null;
    //
    public boolean notiBlocked = true;

    public AppInfo(){

    }

    public AppInfo(String appName, String packageName, int uid, Drawable icon, Intent launchIntent) {
        this.appName = appName;
        this.packageName = packageName;
        this.uid = uid;
        this.icon = icon;
        this.launchIntent = launchIntent;
    }

    public void setIcon(Drawable icon){
        this.icon = icon;
    }

    public void setNotiBlocked(boolean notiBlocked){
        this.notiBlocked = notiBlocked;
    }

}
