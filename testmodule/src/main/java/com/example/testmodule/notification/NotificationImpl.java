package com.example.testmodule.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by mengtao1 on 2017/11/23.
 */

public class NotificationImpl {
    private static final String CHANNEL_ID = "my_channel_id";

    public static void setNotificationChannel(NotificationManager mNotificationManager,
                                               Notification.Builder mBuilder){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID);
            CharSequence name = "Channel name";
            // The user-visible description of the channel.
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_LOW;//not show popup demo
            @SuppressLint("WrongConstant")
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name,importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public static void setNotificationChannel(NotificationManager mNotificationManager,
                                              NotificationCompat.Builder mBuilder){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID);
            CharSequence name = "Channel name";
            // The user-visible description of the channel.
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_LOW;//not show popup demo
            @SuppressLint("WrongConstant")
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name,importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}
