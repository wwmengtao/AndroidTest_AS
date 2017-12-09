package com.example.protoui.travelmode.app;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class PredictedApps {
    private static final String TAG = "PredictedApps";

    private Context mContext;

    private static PredictedApps sInstance = null;

    public static PredictedApps getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PredictedApps.class) {
                if (sInstance == null) {
                    sInstance = new PredictedApps(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private PredictedApps(Context context) {
        mContext = context;
    }

    public List<AppInfo> getAppsInCommute() {
        List<AppInfo> allApps = new ArrayList<>();
        Set<String> uniqueUserPkgs = UserApps.getInstance(mContext).getUserRemovedApps();

        for (AppInfo app : UserApps.getInstance(mContext).getUserAddedApps()) {
            uniqueUserPkgs.add(app.getPackageName());
            allApps.add(app);
        }

        for (String app : AppUsage.getInstance(mContext).getMostUsedAppsInCommute()) {
            // do not include the apps already added by user.
            if (!uniqueUserPkgs.contains(app)) {
                allApps.add(new AppInfo(app));
            }
        }

        return allApps;
    }

    public List<AppInfo> getAppsInPOI(String poi) {
        List<AppInfo> allApps = new ArrayList<>();
        Set<String> uniqueUserPkgs = UserApps.getInstance(mContext).getUserRemovedApps();

        for (AppInfo app : UserApps.getInstance(mContext).getUserAddedApps()) {
            uniqueUserPkgs.add(app.getPackageName());
            allApps.add(app);
        }

        for (String app : AppUsage.getInstance(mContext).getMostUsedAppsInPOI(poi)) {
            // do not include the apps already added by user.
            if (!uniqueUserPkgs.contains(app)) {
                allApps.add(new AppInfo(app));
            }
        }

        return allApps;
    }
}
