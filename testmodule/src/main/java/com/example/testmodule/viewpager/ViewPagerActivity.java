package com.example.testmodule.viewpager;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.EventBusActivity;
import com.example.testmodule.R;
import com.example.testmodule.SQLiteActivity;
import com.example.testmodule.WebViewActivity;
import com.example.testmodule.XmlParserActivity;
import com.example.testmodule.notification.NotifiListActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPagerActivity extends BaseAcitivity {
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6};
    Class<?>[] classEs ={SQLiteActivity.class, WebViewActivity.class, EventBusActivity.class, XmlParserActivity.class,
            NotifiListActivity.class, ViewPagerActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}
