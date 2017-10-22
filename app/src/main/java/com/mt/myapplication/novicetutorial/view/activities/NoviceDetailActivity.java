package com.mt.myapplication.novicetutorial.view.activities;

import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.BaseActivity;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceDetailFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceGridFragment;

public class NoviceDetailActivity extends BaseActivity {
    private Fragment mFragment;
    @Override
    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment getFragment() {
        mFragment = new NoviceDetailFragment();
        return mFragment;
    }

}
