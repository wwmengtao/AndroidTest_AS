package com.mt.androidtest_as;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mt.androidtest_as.myrecyclerview.MyRecyclerViewFragment;


public class MainActivity extends BaseActivity {

    @Override
    public Fragment getFragment() {
        return new MyRecyclerViewFragment();
    }
}
