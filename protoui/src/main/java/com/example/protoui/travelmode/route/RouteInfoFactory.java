package com.example.protoui.travelmode.route;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;


/**
 * Created by huangzq2 on 2017/8/21.
 */

public abstract class RouteInfoFactory {
    protected Context mContext;
    protected int mSuggestionType;
    private boolean mIsReady = false;

    protected Intent mIntent = null;
    protected String mLabel = null;
    protected Drawable mIcon = null;
    protected String mSummary = null;
    protected String mPrice = null;
    protected String mDistance = null;
    protected OnDataLoadListener mOnDataLoadListener = null;

    protected RouteInfoFactory(Context context, int type) {
        mContext = context;
        mSuggestionType = type;
    }

    public void setOnDataLoadListener(OnDataLoadListener mOnDataLoadListener){
        this.mOnDataLoadListener = mOnDataLoadListener;
    }

    public void unSetDataLoadListener(){
        if(null != mOnDataLoadListener)mOnDataLoadListener = null;
    }

    public abstract void requestEstimateInfo();

    public void setReady(boolean ready) {
        mIsReady = ready;
    }

    public boolean isReady() {
        return mIsReady;
    }

    protected abstract RouteInfo createRouteInfo();

    private double oriLat = 41.8781136;
    private double oriLon = -87.6297982;

    protected LatLng getOrigin() {
        LatLng origin = null;
        //此处获取location
        if (origin == null) {
            origin = new LatLng(oriLat, oriLon);
        }
        return origin;
    }

    protected LatLng getDest() {
        LatLng origin = null;
        //此处获取location
        if (origin == null) {
            origin = new LatLng(oriLat+0.1, oriLon+0.04);
        }
        return origin;
    }

    protected LatLng getDestination() {
        LatLng destination = null;
        //此处获取location
        if (destination == null) {
            destination = new LatLng(31.9294066, 118.8000518);
        }
        return destination;
    }

    protected boolean isPackageInstalled(String packageId) {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // ignored.
        }
        return false;
    }

    public interface OnDataLoadListener{
        void onDataLoadSuccess(RouteInfo mRouteInfo);
        void onDataLoadFailed(Throwable t);
    }

    protected String getFinalDistance(Number distance){
        DecimalFormat df = new DecimalFormat("0.00");
        String finalDistance = df.format((Number)distance);
        return finalDistance;
    }
}
