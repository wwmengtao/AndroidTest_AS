package com.example.testmodule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.fernandocejas.android10.sample.data.ALog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotificationCompatActivity extends BaseAcitivity {
    private static final String NOTIFICATION_ID="TestModule.Notification";
    private Unbinder mUnbinder;
    private NotificationManager mNotificationManager = null;
    /** NotificationCompat 构造器*/
    private NotificationCompat.Builder mCompatBuilder;
    @BindView(R.id.btn1) Button btn1;
    @BindView(R.id.btn2) Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_compat);
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
        CharSequence title0 = "Moto";
        CharSequence title = mContext.getResources().getString(R.string.unplugged);
        CharSequence details = mContext.getResources().getString(R.string.unplugged_content);
        int smallIcon = R.drawable.icon;
        mCompatBuilder = new NotificationCompat.Builder(this);
        mCompatBuilder.setWhen(System.currentTimeMillis());
        mCompatBuilder.setAutoCancel(true);
        mCompatBuilder.setTicker(title0);
        mCompatBuilder.setContentTitle(title);
        mCompatBuilder.setContentText(details);
        mCompatBuilder.setContentIntent(intent);
        mCompatBuilder.setSmallIcon(smallIcon);
        mCompatBuilder.setColor(getResources().getColor(R.color.white));//设置smallIcon的背景色)
        mCompatBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cat));
        mCompatBuilder.setOnlyAlertOnce(true);
        mCompatBuilder.setLocalOnly(true);
        mCompatBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mCompatBuilder.setDefaults(Notification.DEFAULT_ALL);
//        NotificationImpl.setNotificationChannel(mNotificationManager, mCompatBuilder);
        Notification mNotification = mCompatBuilder.build();
        try {
            mNotificationManager.notify(NOTIFICATION_ID, id, mNotification);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private ButtonBCReceiver mReceiver;
    private void showNotification2(Context mContext,int id,PendingIntent intent) {
        mReceiver = ButtonBCReceiver.getSwitchBroadcastReceiver(this);
//        NotificationImpl.showButtonNotify(mContext, mNotificationManager, NOTIFICATION_ID, id);
    }

    public void cancelNotification(Context mContext,int id){
        try {
//            mNotificationManager.cancel(id);//只能清除NotificationManager.notify(id, notify);发送的广播
            mNotificationManager.cancel(NOTIFICATION_ID, id);//只能清除NotificationManager.notify(NOTIFICATION_ID, id, notify);发送的广播
//            mNotificationManager.cancelAll();//清除所有格式的广播
            ButtonBCReceiver.unregisterReceiver(this,mReceiver);
        } catch (NullPointerException npe) {
            ALog.Log("setNotificationVisible: cancel notificationManager npe=" + npe);
            npe.printStackTrace();
        }
    }
}
