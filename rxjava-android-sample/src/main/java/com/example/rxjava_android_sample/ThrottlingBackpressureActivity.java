package com.example.rxjava_android_sample;

import android.os.Bundle;
import android.view.View;

import com.example.rxjava_android_sample.throttlingbackpressure.BackpressureActivity;
import com.example.rxjava_android_sample.throttlingbackpressure.ThrottlingActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThrottlingBackpressureActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throttling_backpressure);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    int []buttonIDs={R.id.btn1, R.id.btn2};
    Class<?>[] classEs ={ThrottlingActivity.class, BackpressureActivity.class};
    @OnClick({R.id.btn1, R.id.btn2})
    public void onClickActivity(View view){
        startActivity(view);
    }
}
