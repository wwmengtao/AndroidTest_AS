package com.example.protoui.travelmode.route;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class RouteInfo {
    private String mPackageName;
    private Intent mIntent;
    private String mLabel;
    private Drawable mIcon;
    private String mSummary;

    public RouteInfo(String packageName, Intent intent, String label, Drawable icon, String summary) {
        mPackageName = packageName;
        mIntent = intent;
        mLabel = label;
        mIcon = icon;
        mSummary = summary;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getLabel() {
        return mLabel;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public String getSummary() {
        return mSummary;
    }
}
