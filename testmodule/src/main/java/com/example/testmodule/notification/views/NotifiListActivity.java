package com.example.testmodule.notification.views;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotifiListActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi_list);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3};
    Class<?>[] classEs ={NotificationActivity.class, CusNotificationActivity.class, NotiBlockActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}