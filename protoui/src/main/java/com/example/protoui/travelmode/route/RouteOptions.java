package com.example.protoui.travelmode.route;

import android.content.Context;

import com.example.protoui.travelmode.route.lyft.LyftFactory;
import com.example.protoui.travelmode.route.uber.UberFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class RouteOptions {
    private Context mContext;

    private static RouteOptions sInstance = null;

    public static RouteOptions getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RouteOptions.class) {
                if (sInstance == null) {
                    sInstance = new RouteOptions(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private RouteOptions(Context context) {
        mContext = context;

    }

    public List<RouteInfo> getRouteOptions(int type) {
        List<RouteInfo> routeInfos = new ArrayList<>();
        List<RouteInfoFactory> factories = new ArrayList<>();

        factories.add(new LyftFactory(mContext, type));
        factories.add(new UberFactory(mContext, type));

        // request estimate info asynchronously.
        for (RouteInfoFactory factory : factories) {
            factory.requestEstimateInfo();
        }

        // wait for all results. 10s at most.
        int totalSleep = 0, sleepMs = 500;
        while (!areFactoriesReady(factories) && totalSleep < 10000) {
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException e) {
            }
            totalSleep += sleepMs;
        }

        // return the results.
        for (RouteInfoFactory factory : factories) {
            routeInfos.add(factory.createRouteInfo());
        }

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
