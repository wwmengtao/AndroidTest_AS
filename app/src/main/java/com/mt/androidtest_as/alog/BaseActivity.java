package com.mt.androidtest_as.alog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.androidcommon.alog.CrashBaseActivity;
import com.mt.androidtest_as.R;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public abstract class BaseActivity extends CrashBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceID());
        mContext = getApplicationContext();
        initActionBar();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    public abstract Fragment getFragment();

    @LayoutRes
    protected Integer getResourceID(){//用于规定BaseActivity的布局文件
        return R.layout.activity_base;
    }

    /**
     * initActionBar：如果Activity需要支持ActionBar箭头点击销毁当前Activity功能，那么在Activity中调用此方法
     */
    protected void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
