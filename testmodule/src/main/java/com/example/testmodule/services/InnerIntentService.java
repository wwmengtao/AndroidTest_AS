package com.example.testmodule.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.testmodule.ALog;

/**
 * Created by M.T on 2018/3/27.
 * 用于测试IntentService的一些执行特性
 */

public class InnerIntentService extends IntentService {
    public static final String INTENT_SERVICE_TAG = "InnerIntentService";

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, InnerIntentService.class);
        return intent;
    }

    public InnerIntentService() {
        super(INTENT_SERVICE_TAG);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        ALog.Log("InnerIntentService_onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ALog.Log("InnerIntentService_onHandleIntent");
        ALog.sleep(800);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ALog.Log("InnerIntentService_onDestroy");
    }

}
