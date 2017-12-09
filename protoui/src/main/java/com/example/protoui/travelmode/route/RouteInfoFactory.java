package com.example.protoui.travelmode.route;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.protoui.travelmode.SuggestionRepository;
import com.google.android.gms.maps.model.LatLng;


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

    protected RouteInfoFactory(Context context, int type) {
        mContext = context;
        mSuggestionType = type;
    }

    public abstract void requestEstimateInfo();

    public void setReady(boolean ready) {
        mIsReady = ready;
    }

    public boolean isReady() {
        return mIsReady;
    }

    public abstract RouteInfo createRouteInfo();

    protected LatLng getOrigin() {
        LatLng origin = null;
        if (mSuggestionType == SuggestionRepository.HOME_TO_WORK) {
            origin = ConfirmedPOISDAOImpl.getInstance(mContext).getConfirmedPOILatLng("HOME");
        } else {
            origin = ConfirmedPOISDAOImpl.getInstance(mContext).getConfirmedPOILatLng("WORK");
        }
        if (origin == null) {
            origin = new LatLng(41.8781136, -87.6297982);
        }
        return origin;
    }

    protected LatLng getDestination() {
        LatLng destination = null;
        if (mSuggestionType == SuggestionRepository.HOME_TO_WORK) {
            destination = ConfirmedPOISDAOImpl.getInstance(mContext).getConfirmedPOILatLng("WORK");
        } else {
            destination = ConfirmedPOISDAOImpl.getInstance(mContext).getConfirmedPOILatLng("HOME");
        }
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
}
