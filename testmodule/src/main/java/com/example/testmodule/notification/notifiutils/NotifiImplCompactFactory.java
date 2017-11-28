package com.example.testmodule.notification.notifiutils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.testmodule.R;
import com.example.testmodule.notification.util.RemoteViewUtil;

/**
 * Created by mengtao1 on 2017/11/25.
 */

public class NotifiImplCompactFactory implements NotifiImplFInterface{
    private static NotifiImplCompactFactory mNotifiImplCompactFactory = null;
    Context mContext = null;
    private NotificationManager mNotificationManager = null;

    public static NotifiImplCompactFactory build(Context context){
        if(null == mNotifiImplCompactFactory){
            mNotifiImplCompactFactory = new NotifiImplCompactFactory(context);
        }
        return mNotifiImplCompactFactory;
    }

    private NotifiImplCompactFactory(Context context){
        mContext = context;
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public NotificationImpl get(int style) {
        NotificationImpl mNotificationImpl = new NotificationImpl(mContext);
        mNotificationImpl.setNotificationManager(mNotificationManager);
        mNotificationImpl.initDefaultNotificationCompatBuilder();
        NotificationCompat.Builder mCompatBuilder = mNotificationImpl.getNotificationCompatBuilder();
        switch (style){
            case R.id.btn100://发送一般通知
                NotificationCompat.Action action1 = new NotificationCompat.Action(R.drawable.alert, "Yes", null);
                NotificationCompat.Action action2 = new NotificationCompat.Action(R.drawable.warning, "No", null);
                mNotificationImpl.getNotificationCompatBuilder().addAction(action1);
                mNotificationImpl.getNotificationCompatBuilder().addAction(action2);
                break;
            case R.id.btn101://发送自定义通知
                mCompatBuilder.setContent(RemoteViewUtil.getRemoteViews(mContext));//设置自定义布局
                break;
            case R.id.btn1011://发送自定义音乐播放器通知
                RemoteViews mRemoteViews = RemoteViewUtil.getMusicRemoteView(mContext);
                RemoteViewUtil.PIntentUtil.setOnClickPendingIntentMusic(mContext, mRemoteViews);
                mCompatBuilder.setContent(mRemoteViews)//设置自定义布局
                        .setContentIntent(RemoteViewUtil.PIntentUtil.getDefalutIntent(mContext, Notification.FLAG_ONGOING_EVENT))
                        .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                        .setTicker("正在播放")
                        .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                        .setOngoing(true)//是否常驻通知
                        .setSmallIcon(R.drawable.sing_icon);
                break;
            case R.id.btn102://发送BigTextStyle类型通知
                final String message = mContext.getString(R.string.pip_notification_message);
                NotificationCompat.BigTextStyle nbts = new NotificationCompat.BigTextStyle().bigText(message);
                nbts.setBigContentTitle("BigTextStyle.setBigContentTitle");
                nbts.setSummaryText("BigTextStyle.setSummaryText");
                mNotificationImpl.setBigTextStyle(nbts);
                break;
            case R.id.btn103://发送BigPictureStyle类型通知
                Bitmap bigPicture= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.grassland);//大图
                Bitmap bigLargeIcon=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_fast_rewind_primary_24dp);//图标
                NotificationCompat.BigPictureStyle nbps=new NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(bigLargeIcon)
                        .bigPicture(bigPicture);
                nbps.setBigContentTitle("BigPictureStyle.setBigContentTitle");
                nbps.setSummaryText("BigPictureStyle.setSummaryText");
                mNotificationImpl.setBigPictureStyle(nbps);
                break;
            case R.id.btn104://发送InboxStyle类型通知
                String []conntents = {"1.注意事项:",
                        "2.InboxStyle每行内容过长时不会自动换行",
                        "3.InboxStyle.addline可以添加多行，但是多余5行的时候每行高度会有截断",
                        "4.同BigTextStyle 低版本上系统只能显示普通样式.",
                        "5.Images have four channels, RGBA (red / green / blue / alpha).\n For notification icons, Android ignores the \nR, G, and B channels. ",
                        "6.How Alpha values generate a greyscale image:"};
                NotificationCompat.InboxStyle ibs=new NotificationCompat.InboxStyle();
                for (String conntent : conntents) {
                    ibs.addLine(conntent);
                }
//        ibs.setSummaryText(conntents.length+"条消息");
                ibs.setBigContentTitle("InboxStyle.setBigContentTitle");
                ibs.setSummaryText("InboxStyle.setSummaryText");
                mNotificationImpl.setInboxStyle(ibs);
                break;

        }
        return mNotificationImpl;
    }
}
