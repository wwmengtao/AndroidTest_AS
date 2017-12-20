package com.example.rxjava_android_sample.throttlingbackpressure;

import android.os.Bundle;

import com.example.rxjava_android_sample.BaseAcitivity;
import com.example.rxjava_android_sample.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackpressureActivity extends BaseAcitivity {
    BackpressureExample backpressureExample;//关于反压

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpressure);
        mUnbinder = ButterKnife.bind(this);
        backpressureExample = new BackpressureExample();
    }

    @Override
    protected void onDestroy() {
        backpressureExample.unsubscribe();
        super.onDestroy();
    }

    @OnClick(R.id.tv) void backpressureExampleUnsubscribe() {
        backpressureExample.unsubscribe();
    }

    @OnClick(R.id.button10) void DefaultObservableBuffer() {
        backpressureExample.DefaultObservableBuffer();
    }

    @OnClick(R.id.button11) void MissingBackpressureException() {
        backpressureExample.DefaultObservableBuffer();
    }
    @OnClick(R.id.button12)  void showOnBackpressureBuffer() {
        backpressureExample.onBackpressureBuffer();
    }
    @OnClick(R.id.button13)  void showOnBackpressureDrop() {
        backpressureExample.onBackpressureDrop();
    }
    @OnClick(R.id.button14)  void showOnBackpressureLatest() {
        backpressureExample.onBackpressureLatest();
    }
    @OnClick(R.id.button15)  void showReactivePull() {
        backpressureExample.ReactivePull();
    }

}
