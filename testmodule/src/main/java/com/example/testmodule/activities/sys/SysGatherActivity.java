package com.example.testmodule.activities.sys;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SysGatherActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_gather);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
    Class<?>[] classEs ={LanguageForNActivity.class, LocationActivity.class, PendingActivity.class, SysTraceViewActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}