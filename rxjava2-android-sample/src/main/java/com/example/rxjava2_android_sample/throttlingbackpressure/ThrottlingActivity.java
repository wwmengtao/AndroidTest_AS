package com.example.rxjava2_android_sample.throttlingbackpressure;

import android.os.Bundle;

import com.example.rxjava2_android_sample.BaseAcitivity;
import com.example.rxjava2_android_sample.R;

import butterknife.ButterKnife;

public class ThrottlingActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throttling);
        mUnbinder = ButterKnife.bind(this);
    }
}
