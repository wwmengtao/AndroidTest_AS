package com.example.testmodule.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.testmodule.ALog;
import com.example.testmodule.services.WorkerService;

import static com.example.testmodule.services.WorkerService.WORKTYPE;

/**
 * PackageInstallReceiver：用于监听Apk安装、卸载事件
 * Created by mengtao1 on 2018/1/9.
 */

public class PackageInstallReceiver extends BroadcastReceiver {
    private static final String TAG = "PackageInstallReceiver";
    public static final String PACKAGENAME = "PackageInstallReceiver_Package_Name";
    public static final String WORKTYPE_PACK_ADDED = "PackageInstallReceiver_Package_Added";
    public static final String WORKTYPE_PACK_REMOVED = "PackageInstallReceiver_Package_Removed";
    private static PackageInstallReceiver mReceiver = null;
    private String packageName = null;
    private Intent mIntent = null;
    private IntentFilter mIntentFilter = null;

    public static PackageInstallReceiver getInstance(){
        if(null == mReceiver) {
            mReceiver = new PackageInstallReceiver();
        }
        return mReceiver;
    }

    private PackageInstallReceiver(){
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        mIntentFilter.addDataScheme("package");//非常重要，不加的话收不到广播
    }

    public void registerReceiver(Context context){
        ALog.Log(TAG+"_registerReceiver");
        context.registerReceiver(mReceiver, mIntentFilter);
    }

    public void unRegisterReceiver(Context context){
        ALog.Log(TAG+"_unRegisterReceiver");
        if(null != mReceiver){
            context.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ALog.Log(TAG+"_onReceive: "+intent.getAction());
        packageName = intent.getDataString();
        if(null == packageName)return;
        //以下必须删除原始packageName开头的"package:"，如"package:com.example.rxjava2_android_sample"
        packageName = packageName.replace("package:", "");
        mIntent = WorkerService.newIntent(context);
        mIntent.putExtra(PACKAGENAME, packageName);
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {     // install
            mIntent.putExtra(WORKTYPE, WORKTYPE_PACK_ADDED);
        }else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {   // uninstall
            mIntent.putExtra(WORKTYPE, WORKTYPE_PACK_REMOVED);
        }
        context.startService(mIntent);
    }
}
