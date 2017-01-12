package com.mt.myapplication.beatbox;

import android.support.v4.app.Fragment;
import android.content.res.Configuration;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.BaseActivity;


public class BeatBoxActivity extends BaseActivity {
    @Override
    public Fragment getFragment() {
        return BeatBoxFragment.newInstance();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ALog.Log("onConfigurationChanged",this);
    }
}
