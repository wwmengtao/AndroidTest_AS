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

    private static final String INTENT_EXTRA_PARAM_USER_ID = "org.android10.INTENT_PARAM_USER_ID";
    private static final String INSTANCE_STATE_PARAM_USER_ID = "org.android10.STATE_PARAM_USER_ID";

    public static Intent getCallingIntent(Context context, int userId) {
        Intent callingIntent = new Intent(context, NoviceListActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_USER_ID, userId);
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
        ALog.Log("NoviceListActivity.onUserClicked");
    }
}
