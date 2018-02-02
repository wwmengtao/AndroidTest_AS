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

package com.example.protoui.application;

import com.example.androidcommon.application.CommonApp;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp extends CommonApp {
    private static final String TAG = "ProtoUI_BasicApp_";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化应用异常捕获处理器
//        initCrashManager(MainActivity.class);
    }
}
