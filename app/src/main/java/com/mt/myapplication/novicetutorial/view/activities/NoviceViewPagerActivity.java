package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.view.fragments.BaseFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceViewPagerFragment;

/**
 * NoviceDetailActivity：用于展示每个item的细节
 */
public class NoviceViewPagerActivity extends BaseActivity {
    public static final String NOVICE_VIEWPAGER_ACTIVITY_KEY = "NoviceViewPagerActivity_UserModelNT";
    private BaseFragment mFragment;

    public static Intent getCallingIntent(Context mContext){
        Intent mIntent = new Intent(mContext, NoviceViewPagerActivity.class);
        return mIntent;
    }

    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }


    public Fragment getFragment() {
        mFragment = new NoviceViewPagerFragment();
        return mFragment;
    }

    @Override
    public void finish() {//finish会在onPause之前调用
        mFragment.onActivityFinish();
        super.finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();//会调用finish()
    }
}
