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

package com.example.rxjava2_android_sample.application;

import com.example.androidcommon.alog.ALog;
import com.example.androidcommon.application.CommonApp;
import com.example.rxjava2_android_sample.MainActivity;


/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp extends CommonApp {
    private static final String TAG = "BasicApp_";

    @Override
    public void onCreate() {
        super.onCreate();
        ALog.Log(TAG+"onCreate");
        //初始化应用异常捕获处理器
        initCrashManager(MainActivity.class);
    }
}
