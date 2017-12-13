package com.example.protoui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.protoui.travelmode.LocationUtils;
import com.example.protoui.travelmode.SuggestFactoriesTask;
import com.example.protoui.travelmode.route.RouteInfo;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SuggestFactoriesTask.OnGetListRouteInfoListener {
    private Context mContext = null;
    private LocationUtils mLocationUtils = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_mode);
        mContext = getApplicationContext();
        mLocationUtils = LocationUtils.getInstance(mContext);
        init();
    }

    private void init(){
        SuggestFactoriesTask mTask = new SuggestFactoriesTask(this);
        mTask.setOnGetListRouteInfoListener(this);
        mTask.execute(1);
    }

    @Override
    public void onGetListRouteInfo(List<RouteInfo> result) {
        String packageName = null;
        Intent intent = null;
        String label = null;
        Drawable drawable = null;
        String summary = null;
        for (int i = 0; i < result.size(); i++) {
            RouteInfo route = result.get(i);
            packageName = route.getPackageName();
            intent = route.getIntent();
            label = route.getLabel();
            drawable = route.getIcon();
            summary = route.getSummary();
            ALog.Log("packageName: "+packageName+" label: "+label+" summary: "+summary);
        }
    }
}
