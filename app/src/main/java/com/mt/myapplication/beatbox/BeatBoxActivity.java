package com.mt.myapplication.beatbox;

import android.support.v4.app.Fragment;

import com.mt.androidtest_as.alog.BaseActivity;


public class BeatBoxActivity extends BaseActivity {
    @Override
    public Fragment getFragment() {
        return BeatBoxFragment.newInstance();
    }
}
