package com.example.protoui.travelmode.viewmodel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.example.protoui.R;
import com.example.protoui.travelmode.SuggestionRepository;
import com.example.protoui.travelmode.app.AppInfo;
import com.example.protoui.travelmode.app.PredictedApps;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteOptions;

import java.util.ArrayList;
import java.util.List;


public class CommuteSuggestion {

    private final Context mContext;

    // Suggestion type, such as GO TO WORK, or GO HOME.
    private int mSuggestionType;

    // Contextual label, such as "Heading to work?" or "Heading home".
    public String mContextualLabel;
    public int mContextualLabelResId;

    // Route options
    public List<AppItem> mRouteOptions = new ArrayList<>();
    // Predicted apps
    public List<AppItem> mPredictedApps = new ArrayList<>();

    public CommuteSuggestion(Context context, int type) {
        mContext = context.getApplicationContext();
        mSuggestionType = type;

        if (type == SuggestionRepository.HOME_TO_WORK) {
            mContextualLabel = getContext().getString(R.string.ce_context_go_to_work);
            mContextualLabelResId = R.string.ce_context_go_to_work;
        } else if (type == SuggestionRepository.WORK_TO_HOME) {
            mContextualLabel = getContext().getString(R.string.ce_context_go_home);
            mContextualLabelResId = R.string.ce_context_go_home;
        }
    }

    public int getType() {
        return mSuggestionType;
    }

    public Context getContext() {
        return mContext;
    }

    public CommuteSuggestion buildRouteOptions(boolean build) {
        if (build) {
            List<RouteInfo> routeOptions = RouteOptions.getInstance(getContext()).getRouteOptions(getType());
            for (int i = 0; i < 3 && i < routeOptions.size(); i++) {
                RouteInfo route = routeOptions.get(i);
                addRouteOption(route.getPackageName(), route.getIntent(),
                        route.getLabel(), route.getIcon(), route.getSummary());
            }
        }
        return this;
    }

    public CommuteSuggestion buildPredictedApps(boolean build) {
        if (build) {
            List<AppInfo> predictedApps = PredictedApps.getInstance(getContext()).getAppsInCommute();
            for (int i = 0; i < 3 && i < predictedApps.size(); i++) {
                AppInfo app = predictedApps.get(i);
                addPredictedApp(app.getPackageName(), app.getComponent());
            }
        }
        return this;
    }

    public CommuteSuggestion addRouteOption(String packageName, Intent intent, String label, Drawable icon, String summary) {
        mRouteOptions.add(AppItem.from(getContext(), packageName, null, intent, label, icon, summary));
        return this;
    }

    public CommuteSuggestion addPredictedApp(String packageName, ComponentName componentName) {
        mPredictedApps.add(AppItem.from(getContext(), packageName, componentName, null, null, null, null));
        return this;
    }

    public AppItem getRouteOptions(int index) {
        if (index < mRouteOptions.size()) {
            return mRouteOptions.get(index);
        } else {
            return null;
        }
    }

    public AppItem getPredictedApps(int index) {
        if (index < mPredictedApps.size()) {
            return mPredictedApps.get(index);
        } else {
            return null;
        }
    }
}
