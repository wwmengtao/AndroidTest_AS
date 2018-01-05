package com.example.testmodule.notification.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2018/1/2.
 */

public class AppInfoFetcher {
    private static volatile AppInfoFetcher mAppInfoFetcher = null;
    private Context mContext = null;
    public static final int MAX_APP_NUM = 5;

    public static AppInfoFetcher get(Context context){
        if(null == mAppInfoFetcher){
            mAppInfoFetcher = new AppInfoFetcher(context);
        }
        return mAppInfoFetcher;
    }

    private AppInfoFetcher(Context context){
        this.mContext = mContext;
    }

    public List<AppInfo> getAppInfos(int modeValue){
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        PackageManager pm = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        int mode = modeValue;
        int appIdxStart = mode;
        int max_app_num = MAX_APP_NUM;
        if (infos.size() >= max_app_num*4) {
            appIdxStart = mode*4;
        } else if (infos.size() >= (max_app_num-1)*4) {
            appIdxStart = mode*3;
        } else if (infos.size() >= (max_app_num-2)*4) {
            appIdxStart = mode*2;
        }

        int i = 0;
        int num = 0;
        for(ResolveInfo info  : infos ) {
            if ((i >= appIdxStart) && (num < max_app_num)) {
                appInfos.add(getAppInfo(info, pm));
                num++;
            }
            if (num == max_app_num) {
                break;
            }
            i++;
        }
        return appInfos;
    }

    private AppInfo getAppInfo(ResolveInfo info, PackageManager pm) {
        ActivityInfo ai = info.activityInfo;
        String appName = ai.applicationInfo.loadLabel(pm).toString();
        String packageName = ai.packageName;
        int uid = ai.applicationInfo.uid;
        String className = ai.name;
        Drawable drawable = ai.loadIcon(pm);
        AppInfo appInfo = new AppInfo(appName, packageName, uid, drawable, getLaunchIntent(packageName, className));
        return appInfo;
    }

    private Intent getLaunchIntent(String packageName, String activityName){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(packageName, activityName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//FLAG_ACTIVITY_NEW_TASK一般配合FLAG_ACTIVITY_CLEAR_TOP使用
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

}
