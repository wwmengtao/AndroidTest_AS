package com.example.rxjava2_android_sample;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseAcitivity {
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

    int []buttonIDs={R.id.btn1, R.id.btn2};
    Class<?>[] classEs ={OperatorsActivity.class, NetworkingActivity.class};
    @OnClick({R.id.btn1, R.id.btn2})
    public void onClickActivity(View view){
        startActivity(view);
    }
}
