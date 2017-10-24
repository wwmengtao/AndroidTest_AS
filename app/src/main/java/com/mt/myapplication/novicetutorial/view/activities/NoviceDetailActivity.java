package com.mt.myapplication.novicetutorial.view.activities;

import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceDetailFragment;

public class NoviceDetailActivity extends BaseActivity {
    private Fragment mFragment;

    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }


    public Fragment getFragment() {
        mFragment = new NoviceDetailFragment();
        return mFragment;
    }

}
