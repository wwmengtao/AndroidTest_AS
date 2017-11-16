package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.fragments.BaseFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceDetailFragment;

/**
 * NoviceDetailActivity：用于展示每个item的细节
 */
public class NoviceDetailActivity extends BaseActivity {
    public static final String NOVICE_DETAIL_ACTIVITY_KEY = "NoviceDetailActivity_UserModelNT";
    private BaseFragment mFragment;

    public static Intent getCallingIntent(Context mContext, UserModelNT userModel){
        Intent mIntent = new Intent(mContext, NoviceDetailActivity.class);
        mIntent.putExtra(NOVICE_DETAIL_ACTIVITY_KEY, userModel);
        return mIntent;
    }

    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }


    public Fragment getFragment() {
        mFragment = new NoviceDetailFragment();
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
