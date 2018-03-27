package com.example.testmodule.activities.sys;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;
import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;
import com.example.testmodule.services.InnerIntentService;
import com.example.testmodule.services.WorkerService;
import com.example.testmodule.sysapps.utils.SysAppsManager;

import static com.example.testmodule.receivers.PackageInstallReceiver.PACKAGENAME;
import static com.example.testmodule.receivers.PackageInstallReceiver.WORKTYPE_PACK_ADDED;
import static com.example.testmodule.receivers.PackageInstallReceiver.WORKTYPE_PACK_REMOVED;

public class IntentServiceTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service_test);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent mIntent = InnerIntentService.newIntent(this);
        /**
         * 可以看到下列三次执行InnerIntentService任务时，log信息如下：
         * 03-27 04:02:20.151 17284 17284 E M_T_AT_AS_testmodule.alog: InnerIntentService_onCreate
         * 03-27 04:02:20.152 17284 17335 E M_T_AT_AS_testmodule.alog: InnerIntentService_onHandleIntent
         * 03-27 04:02:20.953 17284 17335 E M_T_AT_AS_testmodule.alog: InnerIntentService_onHandleIntent
         * 03-27 04:02:21.754 17284 17335 E M_T_AT_AS_testmodule.alog: InnerIntentService_onHandleIntent
         * 03-27 04:02:22.556 17284 17284 E M_T_AT_AS_testmodule.alog: InnerIntentService_onDestroy
         */
        startService(mIntent);
        startService(mIntent);
        startService(mIntent);
    }

}
