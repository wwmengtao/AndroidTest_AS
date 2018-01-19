package com.example.testmodule.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.example.testmodule.ALog;

/**
 * PendingService：用于执行延迟操作
 * Created by mengtao1 on 2018/1/19.
 */
public class PendingService extends Service {
    private static final String TAG = "PendingService";
    public static final String EXTRA_PENDING_INTENT = "PendingService_PendingIntent";
    private static Intent mLaunchIntent = null;
    private Intent mIntent = null;

    public static Intent getLaunchIntent(Context context) {
        if(null == mLaunchIntent) {
            mLaunchIntent = new Intent(context, PendingService.class);
        }
        return mLaunchIntent;
    }

    public static boolean isInstanceCreated() {
        return mLaunchIntent != null;
    }//met

    /**
     * getSendToPendingIntent：获取发送短息的PendingIntent
     * @param context：
     * @return
     */
    public static PendingIntent getSendToPendingIntent(Context context){
        PendingIntent pendingIntent;
        Intent newInt;
        newInt = new Intent(Intent.ACTION_SENDTO);
        newInt.setData( Uri.parse( "sms:13913966666"));
        newInt.putExtra("sms_body", "新年快乐，万事如意！");
        pendingIntent = PendingIntent.getActivity(context, 0, newInt, 0);
        return pendingIntent;
    }

    public PendingService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ALog.Log(TAG+"_onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.Log(TAG+"_onStartCommand");
        mIntent = intent;
//        sendTo();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){
        sendTo();
        super.onDestroy();
        ALog.Log(TAG+"_onDestroy");
    }

    //发送短信
    private void sendTo(){
        if(null != mIntent) {
            PendingIntent pi = (PendingIntent) mIntent.getParcelableExtra(EXTRA_PENDING_INTENT);
            if(null == pi)return;
            //如下执行PendingActivity中规定的发送短信动作
            try {
                pi.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

}
