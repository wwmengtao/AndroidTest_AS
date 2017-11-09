package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceListFragment;

public class NoviceListActivity extends BaseActivity implements NoviceListFragment.OnUserClickedListener{
    private Fragment mFragment;

    public static final String NOVICE_LIST_ACTIVITY_KEY = "NoviceDetailActivity_UserModel_KEY";
    public static final String NOVICE_LIST_ACTIVITY_TABLE = "NoviceDetailActivity_UserModel_TABLE";

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
    /**
     * 例如点击call界面的第一条，此时的userModel.toString()内容如下：
     * key: call_mode
     * adjunction: call_mode_description
     * pic: null
     * index: -1
     */
    public void onUserClicked(UserModelNT userModel) {
//        ALog.Log("NoviceListActivity.onUserClicked\n" + userModel.toString());
        UserModelNT mUserModelNTUpLevel = (UserModelNT)getIntent().getParcelableExtra(NOVICE_LIST_ACTIVITY_KEY);
        Intent intent = NoviceDetailActivity.getCallingIntent(this, userModel);
        intent.putExtra(NOVICE_LIST_ACTIVITY_TABLE, mUserModelNTUpLevel.getAdjunction());//传递当前点击数据所在数据表名称
        startActivity(intent);
    }
}
