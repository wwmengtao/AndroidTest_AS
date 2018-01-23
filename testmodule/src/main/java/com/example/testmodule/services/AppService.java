package com.example.testmodule.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.testmodule.ALog;
import com.example.testmodule.application.AppExecutors;
import com.example.testmodule.application.BasicApp;
import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;
import com.example.testmodule.notification.receiver.PackageInstallReceiver;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AppService：全局Service，用于设置各类全局监听事件或者局部数据加载
 * Created by mengtao1 on 2018/1/9.
 */
public class AppService extends Service {
    private static final String TAG = "AppService";
    private Context mContext = null;
    private AppExecutors mAppExecutors = null;
    private static AtomicBoolean isServiceRunning = new AtomicBoolean(false);

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, AppService.class);
    }

    /**
     * isInstanceCreated：判断Service是否被创建(是否在运行)，因为一旦Service被系统回收销毁，整个应用也被销毁
     * @return
     */
    public static boolean isInstanceRunning() {
        return isServiceRunning.get();
    }//met

    public AppService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ALog.Log(TAG+"_onCreate");
        this.mContext = getApplicationContext();
        PackageInstallReceiver.getInstance().registerReceiver(this);
        initData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.Log(TAG+"_onStartCommand");
        isServiceRunning.set(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){//onDestroy并不一定会被调用，当系统资源紧张且当前应用重要性不高的话，整个应用可能会被突然回收
        PackageInstallReceiver.getInstance().unRegisterReceiver(this);
        MockNotifyBlockManager.get(getApplicationContext()).clear();
        super.onDestroy();
        isServiceRunning.set(true);
        ALog.Log(TAG+"_onDestroy");
    }

    private void initData(){
        //1.初始化通知阻塞应用信息
        mAppExecutors = ((BasicApp)getApplication()).getAppExecutors();
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MockNotifyBlockManager.get(mContext);//this will cost much time
            }
        });
    }

}
