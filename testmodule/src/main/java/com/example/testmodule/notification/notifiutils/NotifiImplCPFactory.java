package com.example.testmodule.notification.notifiutils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.testmodule.R;
import com.example.testmodule.notification.receiver.MusicNotifyReceiver;

/**
 * NotifiImplCompactFactory：生产包含NotificationCompat.Builder类型Builder的NotificationImpl的工厂。
 * Created by mengtao1 on 2017/11/25.
 */

public class NotifiImplCPFactory implements NotifiImplFInterface{
    private static NotifiImplCPFactory mNotifiImplCompactFactory = null;
    private Context mContext = null;

    public static NotifiImplCPFactory build(Context context){
        if(null == mNotifiImplCompactFactory){
            mNotifiImplCompactFactory = new NotifiImplCPFactory(context);
        }
        return mNotifiImplCompactFactory;
    }

    private NotifiImplCPFactory(Context context){
        mContext = context;
    }

    @Override
    public NotificationImpl get(int style, int viewID, String viewString) {
        NotificationImpl mNotificationImpl = new NotificationImpl(mContext);
        mNotificationImpl.initDefaultNotificationCompatBuilder();
        NotificationCompat.Builder mCompatBuilder = mNotificationImpl.getNotificationCompatBuilder();
        //setContentIntent:设置用户点击通知整体后的PendingIntent
        mCompatBuilder.setContentIntent(MusicNotifyReceiver.getContentIntentNotification(mContext,
                PendingIntent.FLAG_UPDATE_CURRENT, viewID, viewString));
        //setDeleteIntent：只能监听滑动删除事件，无法监听点击删除(setAutoCancel(true))事件
        mCompatBuilder.setDeleteIntent(MusicNotifyReceiver.getDeleteIntentNotification(mContext,
                PendingIntent.FLAG_UPDATE_CURRENT, viewID, viewString));
        switch (style){
            case 0://发送一般通知
                NotificationCompat.Action action1 = new NotificationCompat.Action(R.drawable.alert, "Yes",
                        MusicNotifyReceiver.getActionIntentYes(mContext, Notification.FLAG_ONGOING_EVENT));
                NotificationCompat.Action action2 = new NotificationCompat.Action(R.drawable.warning, "No",
                        MusicNotifyReceiver.getActionIntentNo(mContext, Notification.FLAG_ONGOING_EVENT));
                mNotificationImpl.getNotificationCompatBuilder().addAction(action1);
                mNotificationImpl.getNotificationCompatBuilder().addAction(action2);
                break;
            case 1://发送自定义通知
                mCompatBuilder.setContent(RemoteViewUtil.getRemoteViews(mContext));//设置自定义布局
                break;
            case 2://发送自定义音乐播放器通知
                RemoteViews mRemoteViews = RemoteViewUtil.getMusicRemoteView(mContext, false);
                MusicNotifyReceiver.setOnClickPendingIntentMusic(mContext, mRemoteViews);
                mCompatBuilder.setContent(mRemoteViews);//设置自定义布局
                //
                mCompatBuilder.setAutoCancel(false);//setAutoCancel：用户点击通知整体(不是某个按钮)后通知是否消失。
                //
                mCompatBuilder.setOngoing(true);//是否常驻通知，即用户是否无法滑动删除
                break;
            case 3://发送BigTextStyle类型通知
                final String message = mContext.getString(R.string.pip_notification_message);
                NotificationCompat.BigTextStyle nbts = new NotificationCompat.BigTextStyle().bigText(message);
                nbts.setBigContentTitle("BigTextStyle.setBigContentTitle");
                nbts.setSummaryText("BigTextStyle.setSummaryText");
                mNotificationImpl.setBigTextStyle(nbts);
                break;
            case 4://发送BigPictureStyle类型通知
                Bitmap bigPicture= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.grassland);//大图
                Bitmap bigLargeIcon=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_fast_rewind_primary_24dp);//图标
                NotificationCompat.BigPictureStyle nbps=new NotificationCompat.BigPictureStyle()
                        .bigLargeIcon(bigLargeIcon)
                        .bigPicture(bigPicture);
                nbps.setBigContentTitle("BigPictureStyle.setBigContentTitle");
                nbps.setSummaryText("BigPictureStyle.setSummaryText");
                mNotificationImpl.setBigPictureStyle(nbps);
                break;
            case 5://发送InboxStyle类型通知
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
