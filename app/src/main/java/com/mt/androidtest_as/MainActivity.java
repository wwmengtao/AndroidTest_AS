package com.mt.androidtest_as;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mt.androidtest_as.data.FLBank;

import java.util.List;


public class MainActivity extends BaseActivity {
    public static final String FRAGMENT_POSITION = "MainActivity_fragment_position";
    public static Intent newIntent(Context mContext, int position){
        Intent mIntent = new Intent(mContext, MainActivity.class);
        mIntent.putExtra(FRAGMENT_POSITION,position);
        return mIntent;
    }

    @Override
    public Fragment getFragment() {
        int positon = getIntent().getIntExtra(FRAGMENT_POSITION, -1);
        Fragment mFragment = getFragment(positon);
        return mFragment;
    }

    private Fragment getFragment(int positon){
        Fragment mFragment = null;
        if(-1 == positon)return new FunctionListFragment();
        List<String> fragmentName = FLBank.get(this).getData();
        String fragmentTitle = fragmentName.get(positon);
        setTitle(fragmentTitle);
        switch (fragmentTitle){
            case "MyRecyclerViewFragment":
                ALog.Log("MyRecyclerViewFragment");
                mFragment = new MyRecyclerViewFragment();
                break;
        }
        return mFragment;
    }
}
