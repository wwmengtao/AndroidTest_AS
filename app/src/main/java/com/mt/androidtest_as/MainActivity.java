package com.mt.androidtest_as;

import android.support.v4.app.Fragment;


public class MainActivity extends BaseActivity {

    @Override
    public Fragment getFragment() {
        return new MyRecyclerViewFragment();
    }
}
