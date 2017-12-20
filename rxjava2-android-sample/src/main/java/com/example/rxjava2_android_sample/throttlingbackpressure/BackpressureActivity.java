package com.example.rxjava2_android_sample.throttlingbackpressure;

import android.os.Bundle;

import com.example.rxjava2_android_sample.BaseAcitivity;
import com.example.rxjava2_android_sample.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackpressureActivity extends BaseAcitivity {
    BackpressureExample backpressureExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpressure);
        mUnbinder = ButterKnife.bind(this);
        backpressureExample = new BackpressureExample();
    }

    @Override
    protected void onStop(){
        super.onStop();
        backpressureExample.unsubscribe();
    }

    @OnClick(R.id.tv) void unsubscribe() {
        backpressureExample.unsubscribe();
    }

    @OnClick(R.id.button10) void ObservableBackpressureTest() {
        backpressureExample.ObservableBackpressureTest();
    }
    @OnClick(R.id.button11) void BackpressureStrategyMissing() {
        backpressureExample.BackpressureStrategyMissing();
    }

    @OnClick(R.id.button12) void BackpressureStrategyError() {
        backpressureExample.BackpressureStrategyError();
    }

    @OnClick(R.id.button13)  void showOnBackpressureBuffer() {
        backpressureExample.BackpressureStrategyBuffer();
    }
    @OnClick(R.id.button14)  void showOnBackpressureDrop() {
        backpressureExample.BackpressureStrategyDrop();
    }
    @OnClick(R.id.button15)  void showOnBackpressureLatest() {
        backpressureExample.BackpressureStrategyLatest();
    }
    @OnClick(R.id.button16)  void pullViaBackpressureStrategy() {
        backpressureExample.pullViaBackpressureStrategy();
    }
}
