package com.example.protoui.travelmode;

import android.content.Context;
import android.os.AsyncTask;

import com.example.protoui.ALog;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteInfoFactory;
import com.example.protoui.travelmode.route.lyft.LyftFactory;
import com.example.protoui.travelmode.route.uber.UberFactory;

import java.util.ArrayList;
import java.util.List;

public class SuggestFactoriesTask extends AsyncTask<Integer, Integer, List<RouteInfo>>{
    private Context mContext = null;
    private OnGetListRouteInfoListener mOnGetListRouteInfoListener = null;
    public SuggestFactoriesTask(Context mContext){
        this.mContext = mContext;
    }

    @Override  
    protected List<RouteInfo> doInBackground(Integer... type) {
        List<RouteInfo> result = getRouteOptions(type[0]);
        return result;
    }
    
    @Override  
    protected void onPostExecute(List<RouteInfo> result) {
        super.onPostExecute(result);
        if(null != mOnGetListRouteInfoListener){
            mOnGetListRouteInfoListener.onGetListRouteInfo(result);
        }
    }  
    
    @Override
    protected void onCancelled(){
    	super.onCancelled();
    }

    public interface OnGetListRouteInfoListener{
        void onGetListRouteInfo(List<RouteInfo> result);
    }

    public void setOnGetListRouteInfoListener(OnGetListRouteInfoListener listener){
        this.mOnGetListRouteInfoListener = listener;
    }

    public List<RouteInfo> getRouteOptions(Integer type) {
        List<RouteInfo> routeInfos = new ArrayList<>();
        List<RouteInfoFactory> factories = new ArrayList<>();

        factories.add(LyftFactory.getInstance(mContext, type));
        factories.add(UberFactory.getInstance(mContext, type));

        // request estimate info asynchronously.
        for (RouteInfoFactory factory : factories) {
            factory.requestEstimateInfo();
        }
        ALog.Log("getRouteOptions1");
        // wait for all results. 10s at most.
        int totalSleep = 0, sleepMs = 500;
        while (!areFactoriesReady(factories) && totalSleep < 10000) {
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
            }
            totalSleep += sleepMs;
        }
        ALog.Log("getRouteOptions2");
        // return the results.
//        for (RouteInfoFactory factory : factories) {
//            routeInfos.add(factory.createRouteInfo());
//        }
//        ALog.Log("getRouteOptions3");
        return routeInfos;
    }

    private boolean areFactoriesReady(List<RouteInfoFactory> factories) {
        for (RouteInfoFactory factory : factories) {
            if (!factory.isReady()) {
                return false;
            }
        }
        return true;
    }
}  
