package com.example.testmodule.activities.sys;

import android.net.Uri;
import android.support.v4.app.Fragment;

import com.example.testmodule.BaseActivity2;

public class SysAppActivity extends BaseActivity2 implements SysAppFragment.OnFragmentInteractionListener{

@Override
    public Fragment getFragment() {
        return new SysAppFragment();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
