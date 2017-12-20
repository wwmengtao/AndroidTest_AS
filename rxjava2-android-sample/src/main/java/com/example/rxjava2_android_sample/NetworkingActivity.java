package com.example.rxjava2_android_sample;

import android.os.Bundle;

import butterknife.ButterKnife;

public class NetworkingActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking);
        mUnbinder = ButterKnife.bind(this);
    }
}
