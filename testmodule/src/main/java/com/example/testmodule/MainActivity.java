package com.example.testmodule;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.activities.data.DataGatherActivity;
import com.example.testmodule.activities.notify.NotifiListActivity;
import com.example.testmodule.activities.sys.SysGatherActivity;
import com.example.testmodule.activities.ui.UIGatherActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        initActivities(buttonIDs, classEs);
    }//end onCreate

    int []buttonIDs={R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
    Class<?>[] classEs ={DataGatherActivity.class, NotifiListActivity.class, SysGatherActivity.class, UIGatherActivity.class};

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void onClickActivity(View view){
        Class<?> activity = mActivitySA.get(view.getId());
        startActivity(getCallingIntent(this, activity));
    }

}
