package com.example.testmodule.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NotifiListActivity extends BaseAcitivity {
    private static final String TAG = "NotifiListActivity";
    private Unbinder mUnbinder;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, NotifiListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi_list);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.btn1)
    public void onClick1(){
        startActivity(getCallingIntent(this, NotificationActivity.class));
    }

    @OnClick(R.id.btn2)
    public void onClick2(){
        startActivity(getCallingIntent(this, NotificationCompatActivity.class));
    }

    @OnClick(R.id.btn3)
    public void onClick3(){
        startActivity(getCallingIntent(this, NotificationCompact2Activity.class));
    }

    @OnClick(R.id.btn4)
    public void onClick4(){
        startActivity(getCallingIntent(this, NotificationOreoActivity.class));
    }
}
