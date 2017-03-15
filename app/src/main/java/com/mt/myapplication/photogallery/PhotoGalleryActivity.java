package com.mt.myapplication.photogallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.BaseActivity;

import static com.mt.myapplication.photogallery.PollService.INTENT_SERVICE_TAG;


public class PhotoGalleryActivity extends BaseActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }

    @Override
    public Fragment getFragment() {
        return PhotoGalleryFragment.newInstance();
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Intent it = getIntent();
        ALog.Log("PhotoGalleryActivity_onNewIntent1: " + it.getStringExtra(INTENT_SERVICE_TAG));//此处为null
        /**
         * PhotoGalleryActivity的launchmode为singleTask、singleTop或者singleInstance的时候，通过各种方式开启已经显示的
         * PhotoGalleryActivity的时候，此时将调用PhotoGalleryActivity.onNewIntent。由于没有调用onCreate函数，因此系统
         * 总是使用第一个打开此Activity的Intent，如果此处不执行setIntent的话，此处或者其他地方执行getIntent()得到的仍然是
         * 旧的(第一次的)intent信息，这样就无法获取最新的intent内容。
         */
        setIntent(intent);
        ALog.Log("PhotoGalleryActivity_onNewIntent2: "+intent.getStringExtra(INTENT_SERVICE_TAG));//此处非null
        it = getIntent();
        ALog.Log("PhotoGalleryActivity_onNewIntent3: "+it.getStringExtra(INTENT_SERVICE_TAG));//此处非null
    }

    public void onResume(){
        super.onResume();
        Intent i = getIntent();
        ALog.Log("PhotoGalleryActivity_onResume: "+i.getStringExtra(INTENT_SERVICE_TAG));
    }
}
