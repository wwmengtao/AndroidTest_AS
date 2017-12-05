package com.example.testmodule;

import android.os.Bundle;

import com.example.testmodule.windowmanager.StatusBarViewManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WindowManagerActivity extends BaseAcitivity{

    private Unbinder mUnbinder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_manager);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn1)
    public void onClick(){
        StatusBarViewManager.getInstance(this).updateView(android.R.color.holo_green_light);
    }

    @OnClick(R.id.btn2)
    public void onClick2(){
        StatusBarViewManager.getInstance(this).removeView();
    }

    @Override
    public void onDestroy() {
        StatusBarViewManager.getInstance(this).onDestroy();
        mUnbinder.unbind();
        super.onDestroy();
    }
}
