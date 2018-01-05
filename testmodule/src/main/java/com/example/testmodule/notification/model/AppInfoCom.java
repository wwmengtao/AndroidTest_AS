package com.example.testmodule.notification.model;

import java.util.Comparator;

/**
 * Created by mengtao1 on 2018/1/5.
 */

public class AppInfoCom implements Comparator {
    @Override
    public int compare(Object o, Object t1) {
        AppInfo info1 = (AppInfo)o;
        AppInfo info2 = (AppInfo)t1;
        int flag = info1.appName.compareTo(info2.appName);
        if(flag==0){
            return info1.packageName.compareTo(info2.packageName);
        }
        return flag;
    }
}
