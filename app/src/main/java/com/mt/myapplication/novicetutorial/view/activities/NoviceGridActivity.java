package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceGridFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceListFragment;

public class NoviceGridActivity extends BaseActivity implements NoviceListFragment.OnUserClickedListener{
    private Fragment mFragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, NoviceListActivity.class);
    }

    @Override
    protected Integer getResourceID() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment getFragment() {
        mFragment = new NoviceGridFragment();
        return mFragment;
    }

    @Override
    public void onUserClicked(UserModelNT userModel) {
        ALog.Log("NoviceGridActivity.onUserClicked");
    }
}
