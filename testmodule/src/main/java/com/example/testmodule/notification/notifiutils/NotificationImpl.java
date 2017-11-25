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
import android.view.View;
import android.widget.RemoteViews;

import com.example.testmodule.R;
import com.example.testmodule.notification.receiver.ButtonBCReceiver;
import com.example.testmodule.notification.receiver.SwitchBCReceiver;
import com.example.testmodule.notification.util.BaseTools;

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

    /**
     * 带按钮的通知栏
     */

    public void showButtonNotify(Context mContext, NotificationManager mNotificationManager,
                                        String NOTIFICATION_ID, int id){
        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_custom_button);
        mRemoteViews.setImageViewResource(R.id.custom_song_icon, R.drawable.sing_icon);
        //API3.0 以上的时候显示按钮，否则消失
        mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, "周杰伦");
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, "七里香");
        //如果版本号低于（3。0），那么不显示按钮
        if(BaseTools.getSystemVersion() <= 9){
            mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.GONE);
        }else{
            mRemoteViews.setViewVisibility(R.id.ll_custom_button, View.VISIBLE);
            //
            if(ButtonBCReceiver.isPlay){
                mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.btn_pause);
            }else{
                mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.drawable.btn_play);
            }
        }

        //点击的事件处理
        Intent buttonIntent = new Intent(ButtonBCReceiver.ACTION_BUTTON_ON_CLICK);
		/* 上一首按钮 */
        buttonIntent.putExtra(ButtonBCReceiver.INTENT_BUTTON_TAG, ButtonBCReceiver.BUTTON_PREV_ID);
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_prev = PendingIntent.getBroadcast(mContext, ButtonBCReceiver.BUTTON_PREV_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_prev);
		/* 播放/暂停  按钮 */
        buttonIntent.putExtra(ButtonBCReceiver.INTENT_BUTTON_TAG, ButtonBCReceiver.BUTTON_PALY_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(mContext, ButtonBCReceiver.BUTTON_PALY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_paly);
		/* 下一首 按钮  */
        buttonIntent.putExtra(ButtonBCReceiver.INTENT_BUTTON_TAG, ButtonBCReceiver.BUTTON_NEXT_ID);
        PendingIntent intent_next = PendingIntent.getBroadcast(mContext, ButtonBCReceiver.BUTTON_NEXT_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(mContext, Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)//是否常驻通知
                .setSmallIcon(R.drawable.sing_icon);
        setNotificationChannel(mNotificationManager, mBuilder);
        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT 表示该通知通知放置在正在运行,不能被手动清除,但能通过 cancel() 方法清除
        //会报错，还在找解决思路
//		notify.contentView = mRemoteViews;
//		notify.contentIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
//        mNotificationManager.notify(id, notify);//这样发送的通知只能用NotificationManager.cancel(int id)清除
        mNotificationManager.notify(NOTIFICATION_ID, id, notify);//这样发送的通知只能用NotificationManager.cancel(String tag, int id)清除
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性:
     * 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public static PendingIntent getDefalutIntent(Context mContext, int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(mContext, 1, new Intent(), flags);
        return pendingIntent;
    }

    public PendingIntent getBCPendingIntent(Context context, String action, int requestCode){
        Intent mIntent = new Intent();
        mIntent.setAction(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    /**
     * setContent：设置自定义布局视图，此操作会覆盖系统界面
     * @param mRemoteViews
     */
    public void setContent(RemoteViews mRemoteViews){
        if(null != mBuilder){
            mBuilder.setContent(mRemoteViews);
        }else if(null != mCompatBuilder){
            mCompatBuilder.setContent(mRemoteViews);
        }
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
    public NotificationCompat.Builder getDefaultNotificationCompatBuilder(Context context){
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
