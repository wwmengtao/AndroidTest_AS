package com.example.protoui.travelmode.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.example.protoui.travelmode.route.lyft.LyftFactory;
import com.example.protoui.travelmode.route.uber.UberFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class UserApps {
    public static final String SHARED_PREFS_NAME = "pref_maya_contextengine";
    public static final String KEY_USER_ADDED_APPS = "user_added_apps";
    public static final String KEY_USER_REMOVED_APPS = "user_removed_apps";

    private Context mContext;
    private final SharedPreferences mSpref;
    private List<UserAppInfo> mUserAddedApps = new ArrayList<>();
    private List<UserAppInfo> mUserRemovedApps = new ArrayList<>();

    private static UserApps sInstance = null;

    public static UserApps getInstance(Context context) {
        if (sInstance == null) {
            synchronized (UserApps.class) {
                if (sInstance == null) {
                    sInstance = new UserApps(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private UserApps(Context context) {
        mContext = context;
        mSpref = mContext.getSharedPreferences(SHARED_PREFS_NAME, 0);
        mUserAddedApps = readUserAddedApps();
        mUserRemovedApps = readUserRemovedApps();
    }

    public List<? extends AppInfo> getUserAddedApps() {
        return mUserAddedApps;
    }

    public void setUserAddedApps(List<? extends AppInfo> newApps) {
        mUserAddedApps.clear();
        for (int i=0; i<newApps.size(); i++) {
            AppInfo app = newApps.get(i);
            mUserAddedApps.add(new UserAppInfo(app.getComponent(), i));
        }
        writeUserAddedApps();
    }

    public void addUserAddedApps(String componentName) {
        ComponentName component = ComponentName.unflattenFromString(componentName);
        if (component != null) {
            mUserAddedApps.add(new UserAppInfo(component, -1));
        }
        writeUserAddedApps();
    }

    public void removeUserAddedApps(String componentName) {
        boolean changed = false;
        ComponentName component = ComponentName.unflattenFromString(componentName);
        Iterator<UserAppInfo> it = mUserAddedApps.iterator();
        while (it.hasNext()) {
            UserAppInfo app = it.next();
            if (component.equals(app.getComponent())) {
                it.remove();
                changed = true;
            }
        }
        if (changed) {
            writeUserAddedApps();
        }
    }

    public void removeUserAppsByPackage(String packageName) {
        boolean changed = false;
        Iterator<UserAppInfo> it = mUserAddedApps.iterator();
        while (it.hasNext()) {
            UserAppInfo app = it.next();
            if (packageName.equals(app.getPackageName())) {
                it.remove();
                changed = true;
            }
        }
        it = mUserRemovedApps.iterator();
        while (it.hasNext()) {
            UserAppInfo app = it.next();
            if (packageName.equals(app.getPackageName())) {
                it.remove();
                changed = true;
            }
        }
        if (changed) {
            writeUserAddedApps();
            writeUserRemovedApps();
        }
    }

    // return the items which were added 3 times.
    public Set<String> getUserRemovedApps() {
        Set<String> validResult = new HashSet<>();

        // Exclude route options
        validResult.add(UberFactory.PACKAGE_NAME);
        validResult.add(LyftFactory.PACKAGE_NAME);

        // Exclude moto app
        validResult.add("com.motorola.moto");

        for (UserAppInfo app : mUserRemovedApps) {
            if (app.mNum >= 3) {
                validResult.add(app.getPackageName());
            }
        }
        return validResult;
    }

    public void addUserRemovedApps(String componentName) {
        boolean found = false;
        ComponentName component = ComponentName.unflattenFromString(componentName);
        if (component != null) {
            for (UserAppInfo app : mUserRemovedApps) {
                if (app != null && component.equals(app.getComponent())) {
                    app.mNum++;
                    found = true;
                }
            }
            if (!found) {
                mUserRemovedApps.add(new UserAppInfo(component, 1));
            }
        } else {
            for (UserAppInfo app : mUserRemovedApps) {
                if (app != null && componentName.equals(app.getPackageName())) {
                    app.mNum++;
                    found = true;
                }
            }
            if (!found) {
                mUserRemovedApps.add(new UserAppInfo(componentName, 1));
            }
        }
        writeUserRemovedApps();
    }

    public void resetUserApps() {
        mUserAddedApps.clear();
        mUserRemovedApps.clear();
        writeUserAddedApps();
        writeUserRemovedApps();
    }

    private boolean isPackageInstalled(String packageId) {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // ignored.
        }
        return false;
    }

    private List<UserAppInfo> readUserAddedApps() {
        Set<String> userApps = getStringSet(KEY_USER_ADDED_APPS, new HashSet<String>());
        List<UserAppInfo> values = new ArrayList<UserAppInfo>();
        for (String app : userApps) {
            UserAppInfo appInfo = UserAppInfo.fromString(app);
            if (isPackageInstalled(appInfo.getPackageName())) {
                values.add(appInfo);
            }
        }
        Collections.sort(values, sPositionComparator);
        return values;
    }

    private void writeUserAddedApps() {
        Set<String> userApps = new HashSet<>();
        for (int i = 0; i< mUserAddedApps.size(); i++) {
            UserAppInfo app = mUserAddedApps.get(i);
            if (app != null) {
                app.mNum = i;
                userApps.add(app.toString());
            }
        }
        putStringSet(KEY_USER_ADDED_APPS, userApps);
    }

    private List<UserAppInfo> readUserRemovedApps() {
        Set<String> userApps = getStringSet(KEY_USER_REMOVED_APPS, new HashSet<String>());
        List<UserAppInfo> values = new ArrayList<UserAppInfo>();
        for (String app : userApps) {
            UserAppInfo appInfo = UserAppInfo.fromString(app);
            if (isPackageInstalled(appInfo.getPackageName())) {
                values.add(appInfo);
            }
        }
        return values;
    }

    private void writeUserRemovedApps() {
        Set<String> userApps = new HashSet<>();
        for (int i = 0; i< mUserRemovedApps.size(); i++) {
            UserAppInfo app = mUserRemovedApps.get(i);
            if (app != null) {
                userApps.add(app.toString());
            }
        }
        putStringSet(KEY_USER_REMOVED_APPS, userApps);
    }


    private final Set<String> getStringSet(String key, Set<String> def) {
        return new HashSet<String>(mSpref.getStringSet(key, def));
    }

    private final void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor edit = mSpref.edit();
        edit.putStringSet(key, value);
        edit.apply();
    }

    private final static Comparator<UserAppInfo> sPositionComparator = new Comparator<UserAppInfo>() {
        public final int compare(UserAppInfo a, UserAppInfo b) {
            if (a.mNum > b.mNum) {
                return -1;
            } else if (a.mNum < b.mNum) {
                return 1;
            }
            return 0;
        }
    };
}