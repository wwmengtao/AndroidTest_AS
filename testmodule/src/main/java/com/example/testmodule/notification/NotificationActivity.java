package com.example.testmodule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotificationActivity extends BaseAcitivity {
    private Unbinder mUnbinder;
    private NotificationManager mNotificationManager = null;
    private String NOTIFICATION_TAG = "TestModule.Notification";//用于标识发送/取消广播时候的tag

    @BindView(R.id.btn1) Button btn1;
    @BindView(R.id.btn2) Button btn2;
    @BindView(R.id.btn3) Button btn3;
    @BindView(R.id.btn32) Button btn32;
    @BindView(R.id.btn4) Button btn4;
    @BindView(R.id.btn5) Button btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mUnbinder = ButterKnife.bind(this);
        btn1.setTag(false);
        btn2.setTag(false);
        btn3.setTag(false);
        btn32.setTag(false);
        btn4.setTag(false);
        btn5.setTag(false);

    }

    @Override
    public void onResume() {
        super.onResume();
//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.holo_purple));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

    NotificationImpl mNotificationImpl1 = null;
    @OnClick(R.id.btn1)
    public void onClick1(View view){
        if(null == mNotificationImpl1)mNotificationImpl1 = getNotificationImpl1();
        if(!(Boolean) btn1.getTag()){
            mNotificationImpl1.sendNotify(1);
            btn1.setTag(true);
        }else{
            mNotificationImpl1.cancelNotify();
            btn1.setTag(false);
        }
    }

    NotificationImpl mNotificationImpl2 = null;
    @OnClick(R.id.btn2)
    public void onClick2(View view){
        if(null == mNotificationImpl2)mNotificationImpl2 = getNotificationImpl2();
        if(!(Boolean) btn2.getTag()){
            mNotificationImpl2.sendNotify(2);
            btn2.setTag(true);
        }else{
            mNotificationImpl2.cancelNotify();
            btn2.setTag(false);
        }
    }

    NotificationImpl mNotificationImpl3 = null;
    @OnClick(R.id.btn3)
    public void onClick3(View view){
        if(null == mNotificationImpl3)mNotificationImpl3 = getNotificationImpl3();
        if(!(Boolean) btn3.getTag()){
            mNotificationImpl3.sendNotify(3);
            btn3.setTag(true);
        }else{
            mNotificationImpl3.cancelNotify();
            btn3.setTag(false);
        }
    }
//
//    @OnClick(R.id.btn32)
//    public void onClick32(View view){
//        if(!(Boolean) btn3.getTag()){
//            showBigTextStyleNotification2(this, 32);
//            btn3.setTag(true);
//        }else{
//            cancelNotification(this, 32);
//            btn3.setTag(false);
//        }
//    }

    NotificationImpl mNotificationImpl4 = null;
    @OnClick(R.id.btn4)
    public void onClick4(View view){
        if(null == mNotificationImpl4)mNotificationImpl4 = getNotificationImpl4();
        if(!(Boolean) btn4.getTag()){
            mNotificationImpl4.sendNotify(4);
            btn4.setTag(true);
        }else{
            mNotificationImpl4.cancelNotify();
            btn4.setTag(false);
        }
    }

    NotificationImpl mNotificationImpl5 = null;
    @OnClick(R.id.btn5)
    public void onClick5(View view){
        if(null == mNotificationImpl5)mNotificationImpl5 = getNotificationImpl5();
        if(!(Boolean) btn5.getTag()){
            mNotificationImpl5.sendNotify(4);
            btn5.setTag(true);
        }else{
            mNotificationImpl5.cancelNotify();
            btn5.setTag(false);
        }
    }

    /**
     * getNotificationImpl1：发送一般通知
     * @return
     */
    private NotificationImpl getNotificationImpl1() {
        NotificationImpl mNotificationImpl = new NotificationImpl(this);
        mNotificationImpl.setNotificationManager(mNotificationManager);
        mNotificationImpl.initDefaultNotificationBuilder();
//        mBuilder.addAction(R.drawable.alert, "Yes", pendingIntentYes);
//        mBuilder.addAction(R.drawable.warning, "No", pendingIntentYes);
        return mNotificationImpl;
    }

    /**
     * getNotificationImpl2：发送自定义通知
     * @return
     */
    private NotificationImpl getNotificationImpl2() {
        NotificationImpl mNotificationImpl = new NotificationImpl(this);
        mNotificationImpl.setNotificationManager(mNotificationManager);
        mNotificationImpl.initDefaultNotificationBuilder();
        mNotificationImpl.setContent(mNotificationImpl.getRemoteViews());//设置自定义布局
        return mNotificationImpl;
    }

    /**
     * getNotificationImpl3：BigTextStyle类型通知
     * @return
     */
    private NotificationImpl getNotificationImpl3() {
        NotificationImpl mNotificationImpl = new NotificationImpl(this);
        mNotificationImpl.setNotificationManager(mNotificationManager);
        mNotificationImpl.initDefaultNotificationBuilder();
        //
        final String message = getString(R.string.pip_notification_message);
        Notification.BigTextStyle nbts = new Notification.BigTextStyle().bigText(message);
        nbts.setSummaryText("描述：可以通过点击向下箭头查看此信息以及设置的大图片");
        nbts.setBigContentTitle("BigTextStyle.setBigContentTitle");
        mNotificationImpl.setBigTextStyle(nbts);
        return mNotificationImpl;
    }

//    @SuppressLint("NewApi")
//    private void showBigTextStyleNotification(Context mContext, int id, PendingIntent intent) {
//        final PackageManager pm = mContext.getPackageManager();
//        final String mPackageName = mContext.getPackageName();
//        ApplicationInfo appInfo = null;
//        try {
//            appInfo = pm.getApplicationInfo(mPackageName, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//
//        }
//
//        if (appInfo != null) {
//            final String appName = pm.getApplicationLabel(appInfo).toString();
//            final String message = mContext.getString(R.string.pip_notification_message, appName);
//            final String ACTION_PICTURE_IN_PICTURE_SETTINGS = "android.settings.PICTURE_IN_PICTURE_SETTINGS";
//            final Intent settingsIntent = new Intent(ACTION_PICTURE_IN_PICTURE_SETTINGS,
//                    Uri.fromParts("package", mPackageName, null));
//            settingsIntent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
//            final Icon appIcon = appInfo.icon != 0
//                    ? Icon.createWithResource(mPackageName, appInfo.icon)
//                    : Icon.createWithResource("cat",R.drawable.cat);
//            Notification.Builder mBuilder =
//                    new Notification.Builder(mContext)
//                            .setLocalOnly(true)
//                            .setOngoing(true)
//                            .setSmallIcon(R.drawable.pip_notification_icon)
//                            .setColor(mContext.getColor(R.color.system_notification_accent_color))
//                            .setTicker("BigTextStyle通知")
//                            .setContentTitle("BigTextStyle通知标题")
//                            .setContentText("BigTextStyle通知正文")
//                            .setContentIntent(PendingIntent.getActivity(mContext, mPackageName.hashCode(),
//                                                                            settingsIntent, FLAG_CANCEL_CURRENT))
//                            .setStyle(new Notification.BigTextStyle().bigText(message))
//                            .setLargeIcon(appIcon);
//            try {
//                NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
//                Notification mNotification = mBuilder.build();
//                mNotificationManager.notify(NOTIFICATION_ID, id, mNotification);
//            } catch (NullPointerException npe) {
//                npe.printStackTrace();
//            }
//        }
//    }
//
//    private void showBigTextStyleNotification2(Context mContext, int id) {
//        //大图
//        Bitmap bigPicture=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cat);
//        //图标
//        Bitmap bigLargeIcon=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.moto);
//        //
//        Notification.Builder mBuilder = new Notification.Builder(mContext);
//        mBuilder.setSmallIcon(R.drawable.icon);
//        mBuilder.setTicker("BigPictureStyle通知");
//        mBuilder.setContentTitle("BigPictureStyle通知标题");
//        mBuilder.setContentText("BigPictureStyle通知正文");
//        final String message = mContext.getString(R.string.pip_notification_message, "M_T");
//        Notification.BigTextStyle nbts = new Notification.BigTextStyle().bigText(message);
//        nbts.setSummaryText("描述：可以通过点击向下箭头查看此信息以及设置的大图片");
//        mBuilder.setStyle(nbts);
//        Intent yesReceive = new Intent();
//        String YES_ACTION = "YES_ACTION";
//        yesReceive.setAction(YES_ACTION);
//        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.addAction(R.drawable.alert, "Yes", pendingIntentYes);
//        mBuilder.addAction(R.drawable.warning, "No", pendingIntentYes);
//        try {
//            NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
//            Notification notification = mBuilder.build();
//            mNotificationManager.notify(NOTIFICATION_ID, id, notification);
//        } catch (Exception npe) {
//            ALog.Log("setNotificationChannel");
//            npe.printStackTrace();
//        }
//    }

    /**
     * getNotificationImpl4：发送BigPictureStyle类型通知
     * @return
     */
    private NotificationImpl getNotificationImpl4() {
        NotificationImpl mNotificationImpl = new NotificationImpl(this);
        mNotificationImpl.setNotificationManager(mNotificationManager);
        mNotificationImpl.initDefaultNotificationBuilder();
        //
        Bitmap bigPicture= BitmapFactory.decodeResource(getResources(), R.drawable.grassland);//大图
        Bitmap bigLargeIcon=BitmapFactory.decodeResource(getResources(), R.drawable.ic_fast_rewind_primary_24dp);//图标
        Notification.BigPictureStyle nbps=new Notification.BigPictureStyle()
                                            .bigLargeIcon(bigLargeIcon)
                                            .bigPicture(bigPicture);
        mNotificationImpl.setBigPictureStyle(nbps);
        return mNotificationImpl;
    }

    /**
     * getNotificationImpl5：发送InboxStyle类型通知
     * @return
     */
    private NotificationImpl getNotificationImpl5() {
        NotificationImpl mNotificationImpl = new NotificationImpl(this);
        mNotificationImpl.setNotificationManager(mNotificationManager);
        mNotificationImpl.initDefaultNotificationBuilder();
        //
        String []conntents = {"1.注意事项:",
                                "2.InboxStyle每行内容过长时不会自动换行",
                                "3.InboxStyle.addline可以添加多行，但是多余5行的时候每行高度会有截断",
                                "4.同BigTextStyle 低版本上系统只能显示普通样式.",
                                "5.Images have four channels, RGBA (red / green / blue / alpha).\n For notification icons, Android ignores the \nR, G, and B channels. ",
                                "6.How Alpha values generate a greyscale image:"};
        Notification.InboxStyle mInboxStyle=new Notification.InboxStyle();
        for (String conntent : conntents) {
            mInboxStyle.addLine(conntent);
        }
        mInboxStyle.setSummaryText(conntents.length+"条消息");
        mInboxStyle.setBigContentTitle("InboxStyle标题");
        mNotificationImpl.setInboxStyle(mInboxStyle);
        return mNotificationImpl;
    }
//
//    private void showInboxStyleNotification(Context mContext, int id) {
//        //大图
//        String []conntents = {"1.注意事项:",
//                                "2.InboxStyle每行内容过长时不会自动换行",
//                                "3.InboxStyle.addline可以添加多行，但是多余5行的时候每行高度会有截断",
//                                "4.同BigTextStyle 低版本上系统只能显示普通样式.",
//                                "5.Images have four channels, RGBA (red / green / blue / alpha).\n For notification icons, Android ignores the \nR, G, and B channels. ",
//                                "6.How Alpha values generate a greyscale image:"};
//        //
//        Notification.Builder mBuilder = new Notification.Builder(mContext);
//        mBuilder.setSmallIcon(R.drawable.ic_phonelink_ring_primary_24dp);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mBuilder.setColorized(true);
//        }
//        mBuilder.setColor(getResources().getColor(R.color.greenyellow));
//        Notification.InboxStyle mInboxStyle=new Notification.InboxStyle(mBuilder);
//        for (String conntent : conntents) {
//            mInboxStyle.addLine(conntent);
//        }
//        mInboxStyle.setSummaryText(conntents.length+"条消息");
//        mInboxStyle.setBigContentTitle("InboxStyle标题");
//        mBuilder.setStyle(mInboxStyle);
//        try {
//            NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
//            Notification notification = mBuilder.build();
//            mNotificationManager.notify(NOTIFICATION_ID, id, notification);
//        } catch (Exception npe) {
//            ALog.Log("setNotificationChannel");
//            npe.printStackTrace();
//        }
//    }


}
