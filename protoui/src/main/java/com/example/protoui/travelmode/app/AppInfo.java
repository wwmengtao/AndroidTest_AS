package com.example.protoui.travelmode.app;

import android.content.ComponentName;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class AppInfo {
    protected String mPackageName;
    protected ComponentName mComponent;

    AppInfo() {
        mPackageName = null;
        mComponent = null;
    }

    AppInfo(String packageName) {
        mComponent = null;
        mPackageName = packageName;
    }

    protected AppInfo(ComponentName component) {
        mComponent = component;
        mPackageName = mComponent.getPackageName();
    }

    public String getPackageName() {
        return mPackageName;
    }

    public ComponentName getComponent() {
        return mComponent;
    }
}
