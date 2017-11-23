package com.example.testmodule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.testmodule.R;
import com.fernandocejas.android10.sample.data.ALog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class NotificationActivity extends AppCompatActivity {
    private static final String NOTIFICATION_ID="TestModule.Notification";
    private Unbinder mUnbinder;
    private NotificationManager mNotificationManager = null;

    @BindView(R.id.btn1) Button btn1;
    @BindView(R.id.btn2) Button btn2;
    @BindView(R.id.btn3) Button btn3;
    @BindView(R.id.btn4) Button btn4;
    @BindView(R.id.btn5) Button btn5;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, NotificationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mUnbinder = ButterKnife.bind(this);
        btn1.setTag(false);
        btn2.setTag(false);
        btn3.setTag(false);
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

    @OnClick(R.id.btn1)
    public void onClick1(View view){
        if(!(Boolean) btn1.getTag()){
            showNotification(this, 1 ,null);
            btn1.setTag(true);
        }else{
            cancelNotification(this, 1);
            btn1.setTag(false);
        }
    }

    @OnClick(R.id.btn2)
    public void onClick2(View view){
        if(!(Boolean) btn2.getTag()){
            showNotification2(this, 2 ,null);
            btn2.setTag(true);
        }else{
            cancelNotification(this, 2);
            btn2.setTag(false);
        }
    }

    @OnClick(R.id.btn3)
    public void onClick3(View view){
        if(!(Boolean) btn3.getTag()){
            showBigTextStyleNotification(this, 3 ,null);
            btn3.setTag(true);
        }else{
            cancelNotification(this, 3);
            btn3.setTag(false);
        }
    }

    @OnClick(R.id.btn4)
    public void onClick4(View view){
        if(!(Boolean) btn4.getTag()){
            showBigPictureStyleNotification(this, 4);
            btn4.setTag(true);
        }else{
            cancelNotification(this, 4);
            btn4.setTag(false);
        }
    }

    @OnClick(R.id.btn5)
    public void onClick5(View view){
        if(!(Boolean) btn5.getTag()){
            showInboxStyleNotification(this, 5);
            btn5.setTag(true);
        }else{
            cancelNotification(this, 5);
            btn5.setTag(false);
        }
    }

    private void showNotification(Context mContext, int id, PendingIntent intent) {
        CharSequence title = "Moto";
        CharSequence title2 = getResources().getString(R.string.unplugged);
        CharSequence text = getResources().getString(R.string.unplugged_content);
        int smallIcon = R.drawable.moto2;
        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setShowWhen(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);
        mBuilder.setTicker(title);
        mBuilder.setContentTitle(title2);
        mBuilder.setContentText(text);
        mBuilder.setContentIntent(intent);
        mBuilder.setSmallIcon(smallIcon);
        mBuilder.setColor(getResources().getColor(R.color.white, null));//设置smallIcon的背景色)
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cat));
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.setLocalOnly(true);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
        Notification mNotification = mBuilder.build();
        try {
            mNotificationManager.notify(NOTIFICATION_ID, id, mNotification);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void showNotification2(Context mContext,int id,PendingIntent intent) {
        int smallIcon = R.drawable.number2_b;
        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setContent(getRemoteViews());
        mBuilder.setShowWhen(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(smallIcon);// 此处设置的图标仅用于显示新提醒时候出现在设备的通知栏
        mBuilder.setColor(getResources().getColor(R.color.whitesmoke,null));//设置smallIcon的背景色)
        mBuilder.setContentTitle("通知的标题");
        mBuilder.setContentText("通知的内容");
        NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
        Notification mNotification = mBuilder.build();
        try {
            mNotificationManager.notify(NOTIFICATION_ID, id, mNotification);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * getRemoteViews：获取特定颜色背景的通知布局
     * @return
     */
    private RemoteViews getRemoteViews(){
        CharSequence title = "Moto";
        CharSequence title2 = getResources().getString(R.string.unplugged);
        CharSequence text = getResources().getString(R.string.unplugged_content);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        mRemoteViews.setImageViewResource(R.id.background, R.drawable.rectangle);
        mRemoteViews.setTextViewText(R.id.title, title);
        mRemoteViews.setTextViewText(R.id.title2, title2);
        mRemoteViews.setTextViewText(R.id.text, text);
        return mRemoteViews;
    }

    private void showBigTextStyleNotification(Context mContext, int id, PendingIntent intent) {
        Notification.Builder mBuilder =
                new Notification.Builder(mContext)
                        .setLocalOnly(true)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.pip_notification_icon)
                        .setColor(mContext.getColor(R.color.system_notification_accent_color));
        final PackageManager pm = mContext.getPackageManager();
        final String mPackageName = mContext.getPackageName();
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo(mPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

        }

        if (appInfo != null) {
            final String appName = pm.getApplicationLabel(appInfo).toString();
            final String message = mContext.getString(R.string.pip_notification_message, appName);
            final String ACTION_PICTURE_IN_PICTURE_SETTINGS = "android.settings.PICTURE_IN_PICTURE_SETTINGS";
            final Intent settingsIntent = new Intent(ACTION_PICTURE_IN_PICTURE_SETTINGS,
                    Uri.fromParts("package", mPackageName, null));
            settingsIntent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            final Icon appIcon = appInfo.icon != 0
                    ? Icon.createWithResource(mPackageName, appInfo.icon)
                    : Icon.createWithResource("cat",R.drawable.cat);

            mBuilder.setContentTitle(mContext.getString(R.string.pip_notification_title, appName))
                    .setContentText(message)
                    .setContentIntent(PendingIntent.getActivity(mContext, mPackageName.hashCode(),
                            settingsIntent, FLAG_CANCEL_CURRENT))
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .setLargeIcon(appIcon);
            try {
                NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
                Notification mNotification = mBuilder.build();
                mNotificationManager.notify(NOTIFICATION_ID, id, mNotification);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }

    private void showBigPictureStyleNotification(Context mContext, int id) {
        //大图
        Bitmap bigPicture=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cat);
        //图标
        Bitmap bigLargeIcon=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.moto);
        //
        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setTicker("showBigPictureStyleNotification");
        mBuilder.setContentTitle("setContentTitle");
        mBuilder.setContentText("setContentText");
        Notification.BigPictureStyle nbps=new Notification.BigPictureStyle(mBuilder)
                                            .bigLargeIcon(bigLargeIcon)
                                            .bigPicture(bigPicture);
        nbps.setSummaryText("Summary text appears on expanding the notification");
        mBuilder.setStyle(nbps);
        try {
            NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
            Notification notification = mBuilder.build();
            mNotificationManager.notify(NOTIFICATION_ID, id, notification);
        } catch (Exception npe) {
            ALog.Log("setNotificationChannel");
            npe.printStackTrace();
        }
    }

    private void showInboxStyleNotification(Context mContext, int id) {
        //大图
        String []conntents = {"1.注意事项:",
                                "2.InboxStyle每行内容过长时不会自动换行",
                                "3.InboxStyle.addline可以添加多行，但是多余5行的时候每行高度会有截断",
                                "4.同BigTextStyle 低版本上系统只能显示普通样式.",
                                "5.Images have four channels, RGBA (red / green / blue / alpha).\n For notification icons, Android ignores the \nR, G, and B channels. ",
                                "6.How Alpha values generate a greyscale image:"};
        //
        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.icon);
        Notification.InboxStyle mInboxStyle=new Notification.InboxStyle(mBuilder);
        for (String conntent : conntents) {
            mInboxStyle.addLine(conntent);
        }
        mInboxStyle.setSummaryText(conntents.length+"条消息");
        mInboxStyle.setBigContentTitle("InboxStyleNotification");
        mBuilder.setStyle(mInboxStyle);
        try {
            NotificationImpl.setNotificationChannel(mNotificationManager, mBuilder);
            Notification notification = mBuilder.build();
            mNotificationManager.notify(NOTIFICATION_ID, id, notification);
        } catch (Exception npe) {
            ALog.Log("setNotificationChannel");
            npe.printStackTrace();
        }
    }

    public void cancelNotification(Context mContext,int id){
        try {
            mNotificationManager.cancel(NOTIFICATION_ID, id);
        } catch (NullPointerException npe) {
            ALog.Log("setNotificationVisible: cancel notificationManager npe=" + npe);
            npe.printStackTrace();
        }
    }
}
