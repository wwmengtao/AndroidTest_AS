package com.mt.androidtest_as;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.data.FLBank;

import java.util.List;


public class MainActivity extends BaseActivity {
    @Override
    public Fragment getFragment() {
        return new FunctionListFragment();
    }
}
