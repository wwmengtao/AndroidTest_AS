package com.mt.androidtest_as;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.alog.BaseActivity;
import com.mt.myapplication.photogallery.PhotoGalleryActivity;
import com.mt.myapplication.photogallery.PollService;
import com.mt.myapplication.photogallery.StartActivitiesReceiver;



public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra(StartActivitiesReceiver.EXTRA_BUNDLE_OF_ACTIVITY);
        //bundle不为空说明是点击通知的时候MainActivity所在的应用还没有运行
        if(bundle != null){
            String name = bundle.getString(StartActivitiesReceiver.MAINACTIVITY_TO_START_ACTIVITY);
            if(null != name && name.equals("PhotoGalleryActivity")){
                Intent it = new Intent(this, PhotoGalleryActivity.class);
                it.putExtra(PollService.INTENT_SERVICE_TAG, PollService.INTENT_SERVICE_TAG);//说明是点击通知信息发起的
                startActivity(it);
            }
        }
    }

    @Override
    public Fragment getFragment() {
        return new FunctionListFragment();
    }
}
