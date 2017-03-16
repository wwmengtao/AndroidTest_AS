package com.mt.myapplication.photogallery;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;

/**
 * PollInfoReceiver:监听PollService发送的广播信息
 */
public class PollInfoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context c, Intent i) {
        ALog.Log("NotificationReceiver_onReceive: " + getResultCode()+" "+i.getAction());
        if (getResultCode() != Activity.RESULT_OK) {//VisibleFragment.mSetResultCodeReceiver中有setResultCode操作
            return;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);
        notificationManager.notify(i.getIntExtra(PollService.REQUEST_CODE, 0), getNotification(c));
    }

    private Notification getNotification(Context mContext){
        Resources resources = mContext.getResources();
        Intent it = new Intent(mContext, StartActivitiesReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(mContext)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        return notification;
    }
}
