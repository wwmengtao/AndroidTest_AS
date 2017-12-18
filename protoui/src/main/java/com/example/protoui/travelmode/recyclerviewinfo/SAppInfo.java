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
    private String summary = null;
    private String description = null;
    private Intent intent = null;

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

    public void setSummary(String summary){
        this.summary = summary;
    }

    public String getSummary(){
        return this.summary;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setSIntent(Intent intent){
        this.intent = intent;
    }
    public Intent getSIntent(){
        return this.intent;
    }

    public static SAppInfo convert(RouteInfo routeInfo){
        ALog.Log("routeInfo: "+routeInfo.getLabel()+" ");
        SAppInfo si = new SAppInfo();
        si.setInfoType(routeInfo.getInfoType());
        si.setTitle(routeInfo.getLabel());
        si.setSummary(routeInfo.getSummary());
        si.setDescription(routeInfo.getPrice());
        si.setDrawable(routeInfo.getIcon());
        si.setPackageName(routeInfo.getPackageName());
        si.setSIntent(routeInfo.getIntent());
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
