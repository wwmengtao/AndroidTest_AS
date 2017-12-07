package com.example.testmodule;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.notification.NotifiListActivity;
import com.example.testmodule.viewpager.ViewPagerActivity;
import com.example.testmodule.windowmanager.WindowManagerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseAcitivity {
    private static final String TAG = "MainActivity";
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

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7};
    Class<?>[] classEs ={SQLiteActivity.class, WebViewActivity.class, EventBusActivity.class, XmlParserActivity.class,
            NotifiListActivity.class, ViewPagerActivity.class, WindowManagerActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}
