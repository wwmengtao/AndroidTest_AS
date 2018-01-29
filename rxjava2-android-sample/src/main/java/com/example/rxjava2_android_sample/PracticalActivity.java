package com.example.rxjava2_android_sample;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.rxjava2_android_sample.fragments.CombineLatestFG;

public class PracticalActivity extends BaseActivity2 {
    public static final String FRAGMENT_NAME = "PracticalActivity_fragment_name";
    public static Intent newIntent(Context mContext, String name){
        Intent mIntent = new Intent(mContext, PracticalActivity.class);
        mIntent.putExtra(FRAGMENT_NAME, name);
        return mIntent;
    }

    @Override
    public Fragment getFragment() {
        String name = getIntent().getStringExtra(FRAGMENT_NAME);
        setTitle(name);
        Fragment mFragment = getFragment(name);
        return mFragment;
    }

    public Fragment getFragment(String name) {
        if(null == name)return null;
        Fragment fg = null;
        switch (name){
            case "CombineLatest":
                fg = new CombineLatestFG();
                break;
        }
        return fg;
    }
}
