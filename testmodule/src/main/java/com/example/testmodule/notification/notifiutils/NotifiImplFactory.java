package com.example.testmodule.notification.notifiutils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.testmodule.R;
import com.example.testmodule.notification.receiver.MusicNotifyReceiver;

/**
 * NotifiImplFactory：生产包含Notification.Builder类型Builder的NotificationImpl的工厂。
 * Created by mengtao1 on 2017/11/25.
 */

public class NotifiImplFactory implements NotifiImplFInterface{
    private static final String NOTIFICATION_GROUP_1 = "NOTIFICATION_GROUP_1";
    private static final String NOTIFICATION_GROUP_2 = "NOTIFICATION_GROUP_2";

    private static NotifiImplFactory mNotifiImplFactory = null;
    private Context mContext = null;
    public static NotifiImplFactory build(Context context){
        if(null == mNotifiImplFactory){
            mNotifiImplFactory = new NotifiImplFactory(context);
        }
        return mNotifiImplFactory;
    }

    private NotifiImplFactory(Context context){
        mContext = context;
    }

    @Override
    public NotificationImpl get(int style, int viewID, String viewString) {
        NotificationImpl mNotificationImpl = new NotificationImpl(mContext);
        mNotificationImpl.initDefaultNotificationBuilder();
        Notification.Builder mBuilder = mNotificationImpl.getNotificationBuilder();
        //setContentIntent:设置用户点击通知整体后的PendingIntent
        mBuilder.setContentIntent(MusicNotifyReceiver.getContentIntentNotification(mContext,
                PendingIntent.FLAG_UPDATE_CURRENT, viewID, viewString));
        //setDeleteIntent：只能监听滑动删除事件，无法监听点击删除(setAutoCancel(true))事件
        mBuilder.setDeleteIntent(MusicNotifyReceiver.getDeleteIntentNotification(mContext,
                PendingIntent.FLAG_UPDATE_CURRENT, viewID, viewString));
        switch (style){
            case 0://一般通知，通知栏置顶显示
                mBuilder.setGroup(NOTIFICATION_GROUP_1);
                Notification.Action action1 = new Notification.Action(
                        R.drawable.alert, "Yes", MusicNotifyReceiver.getActionIntentYes(mContext, Notification.FLAG_ONGOING_EVENT));
                Notification.Action action2 = new Notification.Action(
                        R.drawable.warning, "No", MusicNotifyReceiver.getActionIntentNo(mContext, Notification.FLAG_ONGOING_EVENT));
                mBuilder.addAction(action1);
                mBuilder.addAction(action2);
                //以下设置通知在通知栏置顶显示
                mBuilder.setPriority(Notification.PRIORITY_HIGH).setVibrate(new long[0]);
                Intent push = new Intent();
//                push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                push.setClass(mContext, NotifiListActivity.class);
                PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(mContext, 0,
                        push, PendingIntent.FLAG_CANCEL_CURRENT);
                mBuilder.setFullScreenIntent(fullScreenPendingIntent, true);
                break;
            case 1://可回复通知
                mBuilder.setGroup(NOTIFICATION_GROUP_1);
                //
                RemoteInput remoteInput = new RemoteInput.Builder("KEY_TEXT_REPLY")
                        .setLabel("Reply1")
                        .build();
                PendingIntent replyIntent = PendingIntent.getActivity(mContext,
                        0,
                        getMessageReplyIntent("Reply2"),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Action replyAction =
                        new Notification.Action.Builder(android.R.drawable.sym_def_app_icon,
                                "Reply3", replyIntent)
                                .addRemoteInput(remoteInput)
                                .build();
                mBuilder.addAction(replyAction);
                break;
            case 2://自定义通知
                mBuilder.setGroup(NOTIFICATION_GROUP_1);
                mBuilder.setContent(RemoteViewUtil.getRemoteViews(mContext));//设置自定义布局
                break;
            case 3://BigTextStyle类型通知
                mBuilder.setGroup(NOTIFICATION_GROUP_2);
                final String message = mContext.getString(R.string.pip_notification_message);
                Notification.BigTextStyle nbts = new Notification.BigTextStyle().bigText(message);
                nbts.setBigContentTitle("BigTextStyle.setBigContentTitle");
                nbts.setSummaryText("BigTextStyle.setSummaryText");
                mNotificationImpl.setBigTextStyle(nbts);
                break;
            case 4://BigPictureStyle类型通知
                mBuilder.setGroup(NOTIFICATION_GROUP_2);
                Bitmap bigPicture= BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cat);//大图
                Bitmap bigLargeIcon=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_fast_rewind_primary_24dp);//图标
                Notification.BigPictureStyle nbps=new Notification.BigPictureStyle()
                        .bigLargeIcon(bigLargeIcon)
                        .bigPicture(bigPicture);
                nbps.setBigContentTitle("BigPictureStyle.setBigContentTitle");
                nbps.setSummaryText("BigPictureStyle.setSummaryText");
                mNotificationImpl.setBigPictureStyle(nbps);
                break;
            case 5://InboxStyle类型通知
                mBuilder.setGroup(NOTIFICATION_GROUP_2);
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

    public static final String REPLY_ACTION = "com.hitherejoe.notifi.util.ACTION_MESSAGE_REPLY";
    public static final String KEY_PRESSED_ACTION = "KEY_PRESSED_ACTION";

    private Intent getMessageReplyIntent(String label) {
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(REPLY_ACTION)
                .putExtra(KEY_PRESSED_ACTION, label);
    }
}
