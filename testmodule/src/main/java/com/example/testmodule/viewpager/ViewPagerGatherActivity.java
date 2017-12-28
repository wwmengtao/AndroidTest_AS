package com.example.testmodule.viewpager;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPagerGatherActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_gather);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2};
    Class<?>[] classEs ={ViewPagerActivity.class, ViewPager2Activity.class};

    @OnClick({R.id.btn1, R.id.btn2})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}
