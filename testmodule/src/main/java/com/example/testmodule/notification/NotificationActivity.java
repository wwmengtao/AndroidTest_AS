package com.example.testmodule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

public class NotificationActivity extends AppCompatActivity {
    private static final String NOTIFICATION_ID="TestModule.Notification";
    int OSVersion = android.os.Build.VERSION.SDK_INT;
    private Unbinder mUnbinder;
    private NotificationManager mNotificationManager = null;

    @BindView(R.id.btn1) Button btn1;
    @BindView(R.id.btn2) Button btn2;
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

    private void showNotification(Context mContext, int id, PendingIntent intent) {
        CharSequence title0 = "TestModule";
        CharSequence title = "title";
        CharSequence details = "details";
        int smallIcon = R.drawable.number1_b;
        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setWhen(0);
        mBuilder.setAutoCancel(true);
        mBuilder.setTicker(title0);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(details);
        mBuilder.setContentIntent(intent);
        mBuilder.setSmallIcon(smallIcon);
        mBuilder.setColor(getResources().getColor(R.color.greenyellow));//设置smallIcon的背景色)
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
        mBuilder.setSmallIcon(smallIcon);// 此处设置的图标仅用于显示新提醒时候出现在设备的通知栏
        mBuilder.setContentTitle("通知的标题");
        mBuilder.setContentText("通知的内容");
        mBuilder.setContent(getRemoteViews());
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
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        mRemoteViews.setImageViewResource(R.id.image, R.drawable.rectangle);
        mRemoteViews.setTextViewText(R.id.title, "自定义通知标题");
        mRemoteViews.setTextViewText(R.id.text, "自定义通知内容");
        return mRemoteViews;
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
