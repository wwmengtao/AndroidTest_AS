package com.example.testmodule.notification.notifiutils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.testmodule.R;

/**
 * 通知的高度：普通视图布局限制为 64 dp，扩展视图(setxxxStyle类型的通知视图，例如BigPictureStyle，BigTextStyle)布局限制为 256 dp。
 * Created by mengtao1 on 2017/11/23.
 */

public class NotificationImpl {
    private static final String CHANNEL_ONE_ID = "Channel 1 ID";
    private static final String CHANNEL_ONE_NAME = "Channel 1 Name";
    private static final String CHANNEL_TWO_ID = "Channel 2 ID";
    private static final String CHANNEL_TWO_NAME = "Channel 2 Name";

    private String mNotificationTag = null;//用于标识发送/取消广播时候的tag
    private int mNotificationID = -1;//用于标识发送/取消广播时候的id
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder mCompatBuilder = null;
    private Notification.Builder mBuilder = null;
    private Context mContext = null;
    public NotificationImpl(Context mContext){
        this.mContext = mContext;
        this.mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
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
    public void setNotificationChannel(NotificationManager mNotificationManager, Notification.Builder mBuilder,
                                       String channelID, CharSequence channelName, int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(channelID);
            setNotificationChannel(mNotificationManager, channelID, channelName, importance);
        }
    }

    public void setNotificationChannel(NotificationManager mNotificationManager, NotificationCompat.Builder mBuilder,
                                       String channelID, CharSequence channelName, int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(channelID);
            setNotificationChannel(mNotificationManager, channelID, channelName, importance);
        }

    }

    public void setNotificationChannel(NotificationManager mNotificationManager, String channelID, CharSequence channelName,
                                       int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // The user-visible description of the channel.
            String description = "Channel description";
            @SuppressLint("WrongConstant")
            NotificationChannel mChannel = new NotificationChannel(channelID, channelName, importance);
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

    /**
     * getNotificationBuilder：获取Notification.Builder类型的Builder
     * @return
     */
    @SuppressLint("NewApi")
    private Notification.Builder getDefaultNotificationBuilder(Context context){
        Notification.Builder mBuilder = new Notification.Builder(context);
        CharSequence ticker = "Notification.Builder.ticker";
        CharSequence contentTitle = "Notification.Builder.contentTitle";
        CharSequence ContentText = "Notification.Builder.ContentText";
        int smallIcon = R.drawable.ic_phonelink_ring_primary_24dp;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
        mBuilder.setShowWhen(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setTicker(ticker);
        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(ContentText);
        mBuilder.setOngoing(true);//1、用户无法滑动取消通知
        mBuilder.setAutoCancel(true);//2、用户可以点击取消通知：setAutoCancel设置true必须配合下列setContentIntent才能使得用户点击后通知消失
        mBuilder.setSmallIcon(smallIcon);
        mBuilder.setColor(context.getResources().getColor(R.color.royalblue, null));//设置smallIcon的背景色)
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setLocalOnly(true);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);//优先级越高，在通知栏里面的显示位置越高
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        return mBuilder;
    }



    /**
     * getNotificationCompatBuilder：获取NotificationCompat.Builder类型的Builder
     * @return
     */
    @SuppressLint("NewApi")
    private NotificationCompat.Builder getDefaultNotificationCompatBuilder(Context context){
        CharSequence ticker = "NotificationCompat.Builder.ticker";
        CharSequence contentTitle = "NotificationCompat.Builder.contentTitle";
        CharSequence ContentText = "NotificationCompat.Builder.ContentText";
        int smallIcon = R.drawable.ic_fast_rewind_primary_24dp;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.moto);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, null);
        mBuilder.setShowWhen(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setTicker(ticker);
        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(ContentText);
        mBuilder.setOngoing(false);//1、用户可以滑动取消通知
        mBuilder.setAutoCancel(false);//2、用户无法点击取消通知：setAutoCancel(false)
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
     * sendNotify：发送通知。
     * Notification.Builder构建的通知一律不弹出提示窗口；
     * NotificationCompat.Builder构建的通知一律弹出提示窗口。
     */
    public void sendNotify(int id){
        if(null == mNotificationManager)return;
        mNotificationID = id;
        Notification mNotification = null;
        if(null != mBuilder){
            setNotificationChannel(mNotificationManager, mBuilder, CHANNEL_ONE_ID, CHANNEL_ONE_NAME,
                    NotificationManager.IMPORTANCE_LOW);//NotificationManager.IMPORTANCE_LOW：通知不弹出提示窗口
            mNotification = mBuilder.build();
        }else if(null != mCompatBuilder){
            setNotificationChannel(mNotificationManager, mCompatBuilder, CHANNEL_TWO_ID, CHANNEL_TWO_NAME,
                    NotificationManager.IMPORTANCE_HIGH);//NotificationManager.IMPORTANCE_HIGH：通知弹出提示窗口
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
