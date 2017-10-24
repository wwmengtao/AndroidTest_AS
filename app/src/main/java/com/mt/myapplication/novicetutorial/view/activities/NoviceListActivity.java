package com.mt.myapplication.novicetutorial.view.activities;

import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.androidtest_as.alog.BaseActivity;
import com.mt.myapplication.novicetutorial.model.UserModel;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceGridFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceListFragment;

public class NoviceListActivity extends BaseActivity implements NoviceListFragment.UserListListener{
    private Fragment mFragment;
    @Override
    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment getFragment() {
        mFragment = new NoviceListFragment();
        return mFragment;
    }

    @Override
    public void onUserClicked(UserModel userModel) {
        ALog.Log("NoviceListActivity.onUserClicked");
    }
}
