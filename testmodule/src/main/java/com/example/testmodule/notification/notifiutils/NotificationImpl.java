package com.example.testmodule.notification.notifiutils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.testmodule.R;
import com.example.testmodule.notification.receiver.SwitchBCReceiver;

/**
 * Created by mengtao1 on 2017/11/23.
 */

public class NotificationImpl {
    private static final String NOTIFICATION_CHANNEL_ID = "my_channel_id";
    private String mNotificationTag = null;//用于标识发送/取消广播时候的tag
    private int mNotificationID = -1;//用于标识发送/取消广播时候的id
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder mCompatBuilder = null;
    private Notification.Builder mBuilder = null;
    private Context mContext = null;

    public NotificationImpl(Context mContext){
        this.mContext = mContext;
    }

    public void setNotificationManager(NotificationManager mNotificationManager){
        this.mNotificationManager = mNotificationManager;
    }

    /**
     * initDefaultNotificationBuilder：使用默认的Notification.Builder
     */
    public void initDefaultNotificationBuilder(){
        if(null != mCompatBuilder)return;//不能同时设置两个Builder
        this.mBuilder = getDefaultNotificationBuilder(mContext);
    }

    public void setNotificationBuilder(Notification.Builder mBuilder){
        if(null != mCompatBuilder)return;//不能同时设置两个Builder
        this.mBuilder = mBuilder;
    }

    public Notification.Builder getNotificationBuilder(){
        return (null != mBuilder) ? mBuilder : null;
    }

    public void setBigTextStyle(Notification.BigTextStyle bts){
        if(null != mBuilder)mBuilder.setStyle(bts);
    }

    public void setBigPictureStyle(Notification.BigPictureStyle bps){
        if(null != mBuilder)mBuilder.setStyle(bps);
    }

    public void setInboxStyle(Notification.InboxStyle is){
        if(null != mBuilder)mBuilder.setStyle(is);
    }

    public void initDefaultNotificationCompatBuilder(){
        if(null != mBuilder)return;//不能同时设置两个Builder
        this.mCompatBuilder = getDefaultNotificationCompatBuilder(mContext);
    }

    public void setNotificationCompatBuilder(NotificationCompat.Builder mCompatBuilder){
        if(null != mBuilder)return;//不能同时设置两个Builder
        this.mCompatBuilder = mCompatBuilder;
    }

    public void setBigTextStyle(NotificationCompat.BigTextStyle bts){
        if(null != mCompatBuilder)mCompatBuilder.setStyle(bts);
    }

    public void setBigPictureStyle(NotificationCompat.BigPictureStyle bps){
        if(null != mCompatBuilder)mCompatBuilder.setStyle(bps);
    }

    public void setInboxStyle(NotificationCompat.InboxStyle is){
        if(null != mCompatBuilder)mCompatBuilder.setStyle(is);
    }

    public NotificationCompat.Builder getNotificationCompatBuilder(){
        return (null != mCompatBuilder) ? mCompatBuilder : null;
    }

    /**
     * Build.VERSION_CODES.O require setNotificationChannel
     * @param mNotificationManager
     * @param mBuilder
     */
    public void setNotificationChannel(NotificationManager mNotificationManager,
                                               Notification.Builder mBuilder){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            setNotificationChannel(mNotificationManager);
        }
    }

    public void setNotificationChannel(NotificationManager mNotificationManager,
                                              NotificationCompat.Builder mBuilder){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            setNotificationChannel(mNotificationManager);
        }

    }

    public void setNotificationChannel(NotificationManager mNotificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel name";
            // The user-visible description of the channel.
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_LOW;//not show popup demo
            @SuppressLint("WrongConstant")
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name,importance);
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

    public PendingIntent getBCPendingIntent(Context context, String action, int requestCode){
        Intent mIntent = new Intent();
        mIntent.setAction(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    /**
     * getNotificationBuilder：获取Notification.Builder类型的Builder
     * @return
     */
    @SuppressLint("NewApi")
    private Notification.Builder getDefaultNotificationBuilder(Context context){
        Notification.Builder mBuilder = new Notification.Builder(context);
        CharSequence ticker = "tickertickertickertickertickertickertickertickertickerticker";
        CharSequence contentTitle = "contentTitlecontentTitlecontentTitlecontentTitlecontentTitle";
        CharSequence ContentText = "ContentTextContentTextContentTextContentTextContentTextContentTextContentText";
        int smallIcon = R.drawable.ic_phonelink_ring_primary_24dp;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
        mBuilder.setShowWhen(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        mBuilder.setTicker(ticker);
        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(ContentText);
        mBuilder.setContentIntent(getBCPendingIntent(context, SwitchBCReceiver.ACTION_SWITCH_ON_CLICK,SwitchBCReceiver.SWITCH_ID_DEFAULT));
        mBuilder.setSmallIcon(smallIcon);
        mBuilder.setColor(context.getResources().getColor(R.color.royalblue, null));//设置smallIcon的背景色)
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setLocalOnly(true);
//        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
//        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        //以下两种情况会显示浮动通知: setFullScreenIntent或者通知拥有高优先级且使用了铃声和振动
        mBuilder.setFullScreenIntent(null, false);
        return mBuilder;
    }



    /**
     * getNotificationCompatBuilder：获取NotificationCompat.Builder类型的Builder
     * @return
     */
    @SuppressLint("NewApi")
    private NotificationCompat.Builder getDefaultNotificationCompatBuilder(Context context){
        CharSequence ticker = "tickertickertickertickertickertickertickertickertickerticker";
        CharSequence contentTitle = "contentTitlecontentTitlecontentTitlecontentTitlecontentTitle";
        CharSequence ContentText = "ContentTextContentTextContentTextContentTextContentTextContentTextContentText";
        int smallIcon = R.drawable.ic_phonelink_ring_primary_24dp;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setShowWhen(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        mBuilder.setTicker(ticker);
        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(ContentText);
        mBuilder.setContentIntent(getBCPendingIntent(context,SwitchBCReceiver.ACTION_SWITCH_ON_CLICK,SwitchBCReceiver.SWITCH_ID_DEFAULT));
        mBuilder.setSmallIcon(smallIcon);
        mBuilder.setColor(context.getResources().getColor(R.color.royalblue, null));//设置smallIcon的背景色)
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setLocalOnly(true);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        return mBuilder;
    }

    public void sendNotify(String tag, int id){
        this.mNotificationTag = tag;
        sendNotify(id);
    }

    /**
     * sendNotify：发送通知
     */
    public void sendNotify(int id){
        if(null == mNotificationManager)return;
        mNotificationID = id;
        Notification mNotification = null;
        if(null != mBuilder){
            setNotificationChannel(mNotificationManager, mBuilder);
            mNotification = mBuilder.build();
        }else if(null != mCompatBuilder){
            setNotificationChannel(mNotificationManager, mCompatBuilder);
            mNotification = mCompatBuilder.build();
        }else{
            throw new IllegalArgumentException("Please give not null Notification Builder!");
        }
        if(null != mNotificationTag){
            mNotificationManager.notify(mNotificationTag, id, mNotification);
        }else{
            mNotificationManager.notify(id, mNotification);
        }
    }

    /**
     * cancelNotify：取消通知
     */
    public void cancelNotify(){
        if(null == mNotificationManager)return;
        if(null != mNotificationTag) {
            mNotificationManager.cancel(mNotificationTag, mNotificationID);
        }else{
            mNotificationManager.cancel(mNotificationID);
        }
    }

}
