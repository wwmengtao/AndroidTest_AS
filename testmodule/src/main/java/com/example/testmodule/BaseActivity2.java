package com.example.testmodule;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.androidcommon.alog.CrashBaseActivity;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public abstract class BaseActivity2 extends CrashBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceID());
        mContext = getApplicationContext();
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

}
