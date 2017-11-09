package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceDetailFragment;

public class NoviceDetailActivity extends BaseActivity {
    public static final String DETAIL_ACTIVITY_KEY = "NoviceDetailActivity_UserModelNT";
    private Fragment mFragment;

    public static Intent newIntent(Context mContext, String key){
        Intent mIntent = new Intent(mContext, NoviceDetailActivity.class);
        mIntent.putExtra(DETAIL_ACTIVITY_KEY, key);
        return mIntent;
    }

    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }


    public Fragment getFragment() {
        mFragment = new NoviceDetailFragment();
        return mFragment;
    }

}
