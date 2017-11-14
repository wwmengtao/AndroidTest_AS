package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALog;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.model.MessageEvent;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceListFragment;

import org.greenrobot.eventbus.EventBus;

public class NoviceListActivity extends BaseActivity implements NoviceListFragment.OnUserClickedListener{
    private NoviceListFragment mFragment;

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
     * 例如点击call界面的第六条，此时的userModel.toString()内容如下：
     * /----------------UserModelNT.toString----------------/
     * key: call_mapping
     * adjunction: call_mapping_description
     * pic: lessons6
     * index: -1
     * /----------------UserModelNT.toString----------------/
     */
    public void onUserClicked(UserModelNT userModel) {
        ALog.Log("NoviceListActivity.onUserClicked\n" + userModel.toString());
        boolean byViewPager = mFragment.getPresenter().ifViewItemByViewPager();
        Intent intent = null;
        if(byViewPager){
            intent = NoviceViewPagerActivity.getCallingIntent(this);
            MessageEvent mMessageEvent = new MessageEvent();
            mMessageEvent.setEventType(MessageEvent.EVENT_TYPE.VIEWPAGER);
            mMessageEvent.setData(userModel);
            mMessageEvent.setDataCollection(mFragment.getPresenter().getUserModelNTCollection());
            EventBus.getDefault().postSticky(mMessageEvent);
        }else{
            UserModelNT mUserModelNTUpLevel = (UserModelNT)getIntent().getParcelableExtra(NOVICE_LIST_ACTIVITY_KEY);
            intent = NoviceDetailActivity.getCallingIntent(this, userModel);
            intent.putExtra(NOVICE_LIST_ACTIVITY_TABLE, mUserModelNTUpLevel.getAdjunction());//传递当前点击数据所在数据表名称
        }
        if(null != intent)startActivity(intent);
    }
}
