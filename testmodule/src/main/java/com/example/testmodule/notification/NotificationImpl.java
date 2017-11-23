package com.example.testmodule.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.example.testmodule.R;

/**
 * Created by mengtao1 on 2017/11/23.
 */

public class NotificationImpl {
    private static final String CHANNEL_ID = "my_channel_id";

    /**
     * Build.VERSION_CODES.O require setNotificationChannel
     * @param mNotificationManager
     * @param mBuilder
     */
    public static void setNotificationChannel(NotificationManager mNotificationManager,
                                               Notification.Builder mBuilder){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID);
            setNotificationChannel(mNotificationManager);
        }
    }

    public static void setNotificationChannel(NotificationManager mNotificationManager,
                                              NotificationCompat.Builder mBuilder){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID);
            setNotificationChannel(mNotificationManager);
        }

    }

    public static void setNotificationChannel(NotificationManager mNotificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    /**
     * 带按钮的通知栏
     */

    public static void showButtonNotify(Context mContext, NotificationManager mNotificationManager,
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
}
