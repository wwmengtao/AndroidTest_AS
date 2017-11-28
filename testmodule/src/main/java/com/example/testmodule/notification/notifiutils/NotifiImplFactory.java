package com.example.testmodule.notification.notifiutils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.testmodule.R;
import com.example.testmodule.notification.util.RemoteViewUtil;

/**
 * Created by mengtao1 on 2017/11/25.
 */

public class NotifiImplFactory implements NotifiImplFInterface{
    private static NotifiImplFactory mNotifiImplFactory = null;
    Context mContext = null;
    private NotificationManager mNotificationManager = null;

    public static NotifiImplFactory build(Context context){
        if(null == mNotifiImplFactory){
            mNotifiImplFactory = new NotifiImplFactory(context);
        }
        return mNotifiImplFactory;
    }

    private NotifiImplFactory(Context context){
        mContext = context;
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public NotificationImpl get(int style) {
        NotificationImpl mNotificationImpl = new NotificationImpl(mContext);
        mNotificationImpl.setNotificationManager(mNotificationManager);
        mNotificationImpl.initDefaultNotificationBuilder();
        Notification.Builder mBuilder = mNotificationImpl.getNotificationBuilder();
        switch (style){
            case 0://发送一般通知
                Notification.Action action1 = new Notification.Action(R.drawable.alert, "Yes", null);
                Notification.Action action2 = new Notification.Action(R.drawable.warning, "No", null);
                mNotificationImpl.getNotificationBuilder().addAction(action1);
                mNotificationImpl.getNotificationBuilder().addAction(action2);
                break;
            case 1://发送自定义通知
                mBuilder.setContent(RemoteViewUtil.getRemoteViews(mContext));//设置自定义布局
                break;
            case 2://发送BigTextStyle类型通知
                final String message = mContext.getString(R.string.pip_notification_message);
                Notification.BigTextStyle nbts = new Notification.BigTextStyle().bigText(message);
                nbts.setBigContentTitle("BigTextStyle.setBigContentTitle");
                nbts.setSummaryText("BigTextStyle.setSummaryText");
                mNotificationImpl.setBigTextStyle(nbts);
                break;
            case 3://发送BigPictureStyle类型通知
                Bitmap bigPicture= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.grassland);//大图
                Bitmap bigLargeIcon=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_fast_rewind_primary_24dp);//图标
                Notification.BigPictureStyle nbps=new Notification.BigPictureStyle()
                        .bigLargeIcon(bigLargeIcon)
                        .bigPicture(bigPicture);
                nbps.setBigContentTitle("BigPictureStyle.setBigContentTitle");
                nbps.setSummaryText("BigPictureStyle.setSummaryText");
                mNotificationImpl.setBigPictureStyle(nbps);
                break;
            case 4://发送InboxStyle类型通知
                String []conntents = {"1.注意事项:",
                        "2.InboxStyle每行内容过长时不会自动换行",
                        "3.InboxStyle.addline可以添加多行，但是多余5行的时候每行高度会有截断",
                        "4.同BigTextStyle 低版本上系统只能显示普通样式.",
                        "5.Images have four channels, RGBA (red / green / blue / alpha).\n For notification icons, Android ignores the \nR, G, and B channels. ",
                        "6.How Alpha values generate a greyscale image:"};
                Notification.InboxStyle ibs=new Notification.InboxStyle();
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
