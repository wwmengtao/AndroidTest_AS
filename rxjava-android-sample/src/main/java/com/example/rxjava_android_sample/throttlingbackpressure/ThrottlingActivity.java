package com.example.rxjava_android_sample.throttlingbackpressure;

import android.os.Bundle;

import com.example.rxjava_android_sample.BaseAcitivity;
import com.example.rxjava_android_sample.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThrottlingActivity extends BaseAcitivity {
    ThrottlingExample throttlingExample;//关于节流

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_throttling);
        mUnbinder = ButterKnife.bind(this);
        throttlingExample = new ThrottlingExample();
    }

    @OnClick(R.id.button21)
    void showSample() {
        throttlingExample.showSample();
    }

    @OnClick(R.id.button22)
    void showtTrottleFirst() {
        throttlingExample.showtTrottleFirst();
    }

    @OnClick(R.id.button23)
    void showDebounce() {
        throttlingExample.showDebounce();
    }

    @OnClick(R.id.button24)
    void showtBuffer() {
        throttlingExample.showtBuffer();
    }

    @OnClick(R.id.button25)
    void showtshowtWindow() {
        throttlingExample.showtWindow();
    }
}
