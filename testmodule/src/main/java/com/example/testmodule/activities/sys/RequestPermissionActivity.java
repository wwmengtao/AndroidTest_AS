package com.example.testmodule.activities.sys;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testmodule.BaseActivity2;
import com.example.testmodule.sysapps.views.RequestPermissionsFragment;

public class RequestPermissionActivity extends BaseActivity2 {

    @Override
    public Fragment getFragment() {
        return new RequestPermissionsFragment();
    }
}
