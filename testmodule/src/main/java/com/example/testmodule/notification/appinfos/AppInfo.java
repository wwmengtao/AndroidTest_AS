package com.example.testmodule.notification.appinfos;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Created by mengtao1 on 2018/1/2.
 */

public class AppInfo {
    public final String appName;
    public final String packageName;
    public final String activityName;

    public Intent intent = null;
    public Bitmap icon;
    public int position;

    AppInfo(String appName, String packageName, String activityName, Bitmap icon) {
        this.appName = appName;
        this.packageName = packageName;
        this.activityName = activityName;
        this.icon = icon;

        this.position = -1;
    }

    public void setIntent(Intent intent){
        this.intent = intent;
    }

    public boolean isValid() {
        return icon != null;
    }


    @Override
    public int hashCode() {
        return packageName.hashCode()
                + (intent != null ? intent.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AppInfo other = (AppInfo) obj;
        return this.packageName.equals(other.packageName);
    }

    @Override
    public String toString() {
        return packageName + "," +position;
    }
}
