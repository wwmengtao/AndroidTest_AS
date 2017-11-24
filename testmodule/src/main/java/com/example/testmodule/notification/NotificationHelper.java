package com.example.testmodule.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.testmodule.R;

/**
 * Created by mengtao1 on 2017/11/24.
 */

class NotificationHelper extends ContextWrapper {
    private Context mContext;
    private NotificationManager notifManager;
    public static final String CHANNEL_ONE_ID = "com.jessicathornsby.myapplication.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";
    public static final String CHANNEL_TWO_ID = "com.jessicathornsby.myapplication.TWO";
    public static final String CHANNEL_TWO_NAME = "Channel Two";

//Create your notification channels//


    public NotificationHelper(Context base) {
        super(base);
        mContext = base;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    public void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, notifManager.IMPORTANCE_HIGH);//IMPORTANCE_HIGH：通知会弹出提示
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(notificationChannel);

        NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_TWO_ID,
                CHANNEL_TWO_NAME, notifManager.IMPORTANCE_DEFAULT);//IMPORTANCE_DEFAULT：通知不会弹出提示
        notificationChannel2.enableLights(false);
        notificationChannel2.enableVibration(true);
        notificationChannel2.setLightColor(Color.BLUE);
        notificationChannel2.setShowBadge(false);
        getManager().createNotificationChannel(notificationChannel2);

    }

//Create the notification that’ll be posted to Channel One//

    public Notification.Builder getNotification1(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                    .setShowWhen(true)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("getNotification1")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(mContext.getResources().getColor(R.color.pink,null))
                    .setSmallIcon(R.drawable.alert)
                    .setAutoCancel(true);
        }
        return null;
    }

    //Create the notification that’ll be posted to Channel Two//
    public Notification.Builder getNotification2(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), CHANNEL_TWO_ID)
                    .setShowWhen(true)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("getNotification2")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColorized(true)
                    .setColor(mContext.getResources().getColor(R.color.royalblue,null))
                    .setSmallIcon(R.drawable.warning)
                    .setAutoCancel(true);
        }
        return null;
    }


    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

//Send your notifications to the NotificationManager system service//

    private NotificationManager getManager() {
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }
}

