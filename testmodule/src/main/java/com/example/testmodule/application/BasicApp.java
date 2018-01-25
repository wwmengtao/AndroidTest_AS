/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.testmodule.application;

import android.content.Intent;
import android.os.Build;

import com.example.androidcommon.application.CommonApp;
import com.example.testmodule.ALog;
import com.example.testmodule.MainActivity;
import com.example.testmodule.services.AppService;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp extends CommonApp {
    private static final String TAG = "BasicApp_";
    private Intent startServiceIntent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        ALog.Log(TAG+"onCreate");
        this.startServiceIntent = AppService.getLaunchIntent(this);
        //初始化应用异常捕获处理器
        initCrashManager(MainActivity.class);
    }


    @Override
    protected void notifyForeground() {
        super.notifyForeground();
        //AppService.isInstanceCreated()可以保证每次应用show到前台的时候，AppService能存在且正在运行
        tryToStartService();
    }

    @Override
    protected void notifyScreenOn() {
        super.notifyScreenOn();
        if (!isBackground()) {//只有前台应用监测屏幕点亮才有意义
            tryToStartService();
        }
    }

    private void tryToStartService(){
        if(!AppService.isInstanceRunning()){
            try {
                startService(startServiceIntent);
            }catch (IllegalStateException ie){
                //详见为知笔记“Android 8.0后台执行限制”一文
                //Android8.0错误描述：java.lang.RuntimeException: Unable to create application com.example.testmodule.application.BasicApp: java.lang.IllegalStateException: Not allowed to start service Intent { cmp=com.example.testmodule/.services.AppService }: app is in background uid UidRecord{4c36b u0a133 CEM  idle procs:1 seq(0,0,0)}
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(startServiceIntent);
                }
            }//end try-catch
        }//end if
    }//end tryToStartService
}
