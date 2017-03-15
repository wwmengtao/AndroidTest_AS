package com.mt.myapplication.photogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.photogallery.data.QueryPreferences;

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ALog.Log("StartupReceiver_onReceive: " + intent.getAction());
        boolean isOn = QueryPreferences.isAlarmOn(context);
        PollService.setServiceAlarm(context, isOn);
    }

}
