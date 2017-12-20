package com.example.rxjava_android_sample;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2};
    Class<?>[] classEs ={OperatorsActivity.class, ThrottlingBackpressureActivity.class};
    @OnClick({R.id.btn1, R.id.btn2})
    public void onClickActivity(View view){
        startActivity(view);
    }
}
