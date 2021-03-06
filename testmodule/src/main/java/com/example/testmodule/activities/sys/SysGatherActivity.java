package com.example.testmodule.activities.sys;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SysGatherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_gather);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10};
    Class<?>[] classEs ={LanguageForNActivity.class, LocationActivity.class, PendingActivity.class, SysTraceViewActivity.class,
            SysAppActivity.class, TouchEventActivity.class, ANRActivity.class, RequestPermissionActivity.class,
            OOMActivity.class, IntentServiceTestActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}
