package com.example.protoui.travelmode.route;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class RouteInfo {
    private InfoType mInfoType;
    private String mPackageName;
    private Intent mIntent;
    private String mLabel;
    private String mPrice;
    private Drawable mIcon;
    private String mSummary;
    private String mDistance;

    public enum InfoType{
        UBER,
        LYFT
    }

    public RouteInfo(InfoType infoType, String packageName, Intent intent, String label, String price,
                     Drawable icon, String summary, String distance) {
        mInfoType = infoType;
        mPackageName = packageName;
        mIntent = intent;
        mLabel = label;
        mPrice = price;
        mIcon = icon;
        mSummary = summary;
        mDistance = distance;
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

    public String getPrice() {
        return mPrice;
    }

    public InfoType getInfoType(){
        return this.mInfoType;
    }

    public String getDistance(){
        return mDistance;
    }
}
