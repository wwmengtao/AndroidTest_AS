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
        /**
         * launchmode为singleTask、singleTop或者singleInstance的时候，不执行setIntent的话，getIntent得到的仍然是
         * 旧的intent信息，这样就无法获取最新的intent内容。
         */
        setIntent(intent);//
        ALog.Log("PhotoGalleryActivity_onNewIntent: "+intent.getStringExtra(INTENT_SERVICE_TAG));
    }

    public void onResume(){
        super.onResume();
        Intent i = getIntent();
        ALog.Log("PhotoGalleryActivity_onResume: "+i.getStringExtra(INTENT_SERVICE_TAG));
    }
}
