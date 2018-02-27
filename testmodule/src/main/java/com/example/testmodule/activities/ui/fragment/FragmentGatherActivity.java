package com.example.testmodule.activities.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentGatherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_gather);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }

    int []buttonIDs={R.id.btn1, R.id.btn2};
    Class<?>[] classEs ={FragmentTestActivity.class, FragmentTest2Activity.class};

    @OnClick({R.id.btn1, R.id.btn2})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }
}
