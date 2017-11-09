package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceListFragment;

public class NoviceListActivity extends BaseActivity implements NoviceListFragment.OnUserClickedListener{
    private Fragment mFragment;

    public static final String NOVICE_LIST_ACTIVITY_KEY = "NoviceDetailActivity_UserModelNT";

    public static Intent getCallingIntent(Context context, UserModelNT userModel) {
        Intent callingIntent = new Intent(context, NoviceListActivity.class);
        callingIntent.putExtra(NOVICE_LIST_ACTIVITY_KEY, userModel);
        return callingIntent;
    }

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
    public void onUserClicked(UserModelNT userModel) {
        ALog.Log("NoviceListActivity.onUserClicked\n" + userModel.toString());
        Intent intent = NoviceDetailActivity.getCallingIntent(this, userModel);
        startActivity(intent);
    }
}
