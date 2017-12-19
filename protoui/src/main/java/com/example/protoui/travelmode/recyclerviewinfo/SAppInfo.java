package com.example.protoui.travelmode.recyclerviewinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.example.protoui.ALog;
import com.example.protoui.R;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteInfo.InfoType;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to show suggested app details
 * Created by mengtao1 on 2017/12/11.
 */

public class SAppInfo {
    private InfoType mInfoType = null;
    private String packageName = null;
    private Drawable drawable = null;
    private String title = null;
    private String price = null;
    private String waitingTime = null;
    private Intent intent = null;
    private String distance = null;
    public SAppInfo(){

    }

    public void setInfoType(InfoType mInfoType){
        this.mInfoType = mInfoType;
    }

    public InfoType getInfoType(){
        return this.mInfoType;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public String getPackageName(){
        return this.packageName;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
    public void setDrawable(Drawable drawable){
        this.drawable = drawable;
    }

    public Drawable getDrawable(){
        return this.drawable;
    }

    public void setPrice(String summary){
        this.price = summary;
    }

    public String getPrice(){
        return this.price;
    }

    public void setWaitingTime(String description){
        this.waitingTime = description;
    }

    public String getWaitingTime(){
        return this.waitingTime;
    }

    public void setSIntent(Intent intent){
        this.intent = intent;
    }
    public Intent getSIntent(){
        return this.intent;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }
    public String getDistance(){
        return this.distance;
    }

    public static SAppInfo convert(RouteInfo routeInfo){
        ALog.Log("routeInfo: "+routeInfo.getDistance());
        SAppInfo si = new SAppInfo();
        si.setInfoType(routeInfo.getInfoType());
        si.setTitle(routeInfo.getLabel());
        si.setWaitingTime(routeInfo.getSummary());
        si.setPrice(routeInfo.getPrice());
        si.setDrawable(routeInfo.getIcon());
        si.setPackageName(routeInfo.getPackageName());
        si.setSIntent(routeInfo.getIntent());
        si.setDistance(routeInfo.getDistance());
        return si;
    }

    public static List<SAppInfo> getInitData(Context context){
        List<SAppInfo> mData = new ArrayList<>();
        SAppInfo si = new SAppInfo();
        si.setDrawable(context.getDrawable(R.drawable.googleg_color));
        mData.add(si);
        mData.add(si);
        return mData;
    }
}
