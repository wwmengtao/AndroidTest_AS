package com.example.protoui.travelmode.app;

import android.content.ComponentName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class UserAppInfo extends AppInfo {
    private static final String SEPARATOR = "&";
    private static final Pattern FAVORITE_PATTERN = Pattern.compile("(.+)&(-?[0-9]+)");

    int mNum;

    UserAppInfo(ComponentName component, int position) {
        super(component);
        mNum = position;
    }

    UserAppInfo(String packageName, int position) {
        super(packageName);
        mNum = position;
    }

    @Override
    public String toString() {
        return (mComponent != null ? mComponent.flattenToShortString() : mPackageName) + SEPARATOR + mNum;
    }

    public static UserAppInfo fromString(String input) {
        if (input == null || input.isEmpty())
            return null;

        ComponentName component = null;
        String packageName = null;
        int position = -1;
        Matcher m = FAVORITE_PATTERN.matcher(input);
        if (m.find()) {
            component = ComponentName.unflattenFromString(m.group(1));
            if (component == null) {
                packageName = m.group(1);
            } else {
                packageName = component.getPackageName();
            }
            position = Integer.parseInt(m.group(2));
            if (component != null) {
                return new UserAppInfo(component, position);
            } else {
                return new UserAppInfo(packageName, position);
            }
        } else {
            return null;
        }
    }
}
