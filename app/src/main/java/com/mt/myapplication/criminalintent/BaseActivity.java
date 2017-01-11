package com.mt.myapplication.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.alog.ALogActivity;

/**
 * Created by Mengtao1 on 2016/12/8.
 */

public abstract class BaseActivity extends ALogActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceID());
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
    protected Integer getResourceID(){
        return R.layout.activity_crime;
    }
}
