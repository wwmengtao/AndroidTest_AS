package com.mt.myapplication.photogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mt.androidtest_as.MainActivity;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.allcommon.tool.SystemUtils;

import static com.mt.myapplication.allcommon.tool.SystemUtils.CURRENT_APP_PACKAGENAME;
import static com.mt.myapplication.photogallery.PollService.INTENT_SERVICE_TAG;

/**
 * StartActivitiesReceiver:用于决定点击通知栏通知后，如何开启以及开启哪些activity
 */
public class StartActivitiesReceiver extends BroadcastReceiver{
    public static final String EXTRA_BUNDLE_OF_ACTIVITY = "EXTRA_BUNDLE_OF_ACTIVITY";
    public static final String MAINACTIVITY_TO_START_ACTIVITY = "MAINACTIVITY_TO_START_ACTIVITY";
    @Override
    public void onReceive(Context context, Intent intent) {
        //判断进程"com.mt.androidtest_as"是否存活
        if(SystemUtils.isAppAlive(context, CURRENT_APP_PACKAGENAME)){
            ALog.Log("StartActivitiesReceiver_onReceive");
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent detailIntent = new Intent(context, PhotoGalleryActivity.class);
            detailIntent.putExtra(INTENT_SERVICE_TAG, INTENT_SERVICE_TAG);//说明是点击通知信息发起的
            Intent[] intents = {mainIntent, detailIntent};
            context.startActivities(intents);
        }else {
            ALog.Log("StartActivitiesReceiver_onReceive2");
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage(CURRENT_APP_PACKAGENAME);
            Bundle args = new Bundle();
            args.putString(MAINACTIVITY_TO_START_ACTIVITY, "PhotoGalleryActivity");
            launchIntent.putExtra(EXTRA_BUNDLE_OF_ACTIVITY, args);
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        }
    }
}
