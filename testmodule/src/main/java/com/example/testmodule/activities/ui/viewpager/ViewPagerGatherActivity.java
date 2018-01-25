package com.example.testmodule.activities.ui.viewpager;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;
import com.example.testmodule.activities.ui.viewpager.fragment.ViewPagerFragmentActivity;
import com.example.testmodule.activities.ui.viewpager.fragment.ViewPagerStateFragmentActivity;

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

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
    Class<?>[] classEs ={ViewPagerActivity.class, ViewPager2Activity.class, ViewPagerFragmentActivity.class,
            ViewPagerStateFragmentActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}
