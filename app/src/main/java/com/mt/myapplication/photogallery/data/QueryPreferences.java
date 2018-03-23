package com.mt.myapplication.photogallery.data;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {

    private static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();//commit和apply的方法区别在于同步写入和异步写入，以及是否需要返回值。Editor的apply方法，每次执行时在单线程池中加入写入磁盘Task，异步写入。可以提高写入性能。
    }

}
