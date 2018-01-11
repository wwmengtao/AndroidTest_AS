package com.example.testmodule.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.testmodule.ALog;
import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;

import static com.example.testmodule.notification.receiver.PackageInstallReceiver.PACKAGENAME;
import static com.example.testmodule.notification.receiver.PackageInstallReceiver.WORKTYPE_PACK_ADDED;
import static com.example.testmodule.notification.receiver.PackageInstallReceiver.WORKTYPE_PACK_REMOVED;

/**
 * WorkerService：工作类型的IntentService，用于处理各类后台耗时事件
 * Created by mengtao1 on 2018/1/9.
 */
public class WorkerService extends IntentService {
    public static final String INTENT_SERVICE_TAG = "WorkerService";
    public static final String WORKTYPE = "WorkerService_Work_Type";
    private Context mContext = null;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WorkerService.class);
        return intent;
    }

    public WorkerService() {
        super(INTENT_SERVICE_TAG);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ALog.Log("WorkerService_onCreate");
        mContext = getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String type = intent.getStringExtra(WORKTYPE);
        if(null == type)return;
        switch (type){
            case WORKTYPE_PACK_ADDED:
            case WORKTYPE_PACK_REMOVED:
                refreshPakcageInfo(intent);
                break;
        }
    }

    //更新存储的应用信息
    private void refreshPakcageInfo(Intent intent){
        String workType = intent.getStringExtra(WORKTYPE);
        String packageName = intent.getStringExtra(PACKAGENAME);
        ALog.Log("workType: "+workType+" packageName: "+packageName);
        switch (workType){
            case WORKTYPE_PACK_ADDED:
                MockNotifyBlockManager.get(mContext).onPackageInstalled(packageName);
                break;
            case WORKTYPE_PACK_REMOVED:
                MockNotifyBlockManager.get(mContext).onPackageUnInstalled(packageName);
                break;
        }
    }
}
