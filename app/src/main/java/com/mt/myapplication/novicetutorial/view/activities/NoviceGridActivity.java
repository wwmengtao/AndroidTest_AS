package com.mt.myapplication.novicetutorial.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.R;
import com.mt.myapplication.novicetutorial.com.fernandocejas.android10.sample.presentation.model.UserModelNT;
import com.mt.myapplication.novicetutorial.view.fragments.BaseFragment;
import com.mt.myapplication.novicetutorial.view.fragments.NoviceGridFragment;

public class NoviceGridActivity extends BaseActivity implements BaseFragment.OnFragmentClickListener{
    private BaseFragment mFragment;

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
    /**
     *例如点击主界面上的call，此时的userModel.toString()内容如下：
     key: call.xml
     adjunction: call
     pic: novicetutorial/title1_pics/call.png
     index: -1
     上述adjunction即为NoviceListActivity所显示全部数据的数据库中数据表名称。
     */
    public void onFragmentClicked(UserModelNT userModel) {
//        ALog.Log("NoviceGridActivity.onUserClicked:\n"+userModel.toString());
//        ALog.Log("NoviceGridActivity.onUserClicked: "+userModel.getKey()+" "+userModel.getIndex());
        Intent intent = NoviceListActivity.getCallingIntent(this, userModel);
        this.startActivity(intent);
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
