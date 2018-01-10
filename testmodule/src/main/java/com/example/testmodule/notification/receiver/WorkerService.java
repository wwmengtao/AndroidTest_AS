package com.example.testmodule.notification.receiver;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;

import static com.example.testmodule.notification.receiver.PackageInstallReceiver.PACKAGENAME;
import static com.example.testmodule.notification.receiver.PackageInstallReceiver.WORKTYPE_PACK_ADDED;
import static com.example.testmodule.notification.receiver.PackageInstallReceiver.WORKTYPE_PACK_REMOVED;

public class WorkerService extends IntentService {
    public static final String INTENT_SERVICE_TAG = "WorkerService";
    public static final String WORKTYPE = "WorkerService_Work_Type";


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WorkerService.class);
        return intent;
    }

    public WorkerService() {
        super(INTENT_SERVICE_TAG);
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

    private void refreshPakcageInfo(Intent intent){
        String workType = intent.getStringExtra(WORKTYPE);
        String packageName = intent.getStringExtra(PACKAGENAME);
        MockNotifyBlockManager mMNBM = MockNotifyBlockManager.get(getApplicationContext());
        switch (workType){
            case WORKTYPE_PACK_ADDED:
                mMNBM.onPackageInstalled(packageName);
                break;
            case WORKTYPE_PACK_REMOVED:
                mMNBM.onPackageUnInstalled(packageName);
                break;
        }
//        mMNBM.
    }
}
