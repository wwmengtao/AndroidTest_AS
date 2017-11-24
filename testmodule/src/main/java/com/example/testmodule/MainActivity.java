package com.example.testmodule;

import android.os.Bundle;
import android.view.View;

import com.example.testmodule.notification.NotifiListActivity;

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
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick(R.id.btn1)
    public void onClick1(View view){
        getCallingIntent(this,SQLiteActivity.class);
        startActivity(getCallingIntent(this,SQLiteActivity.class));
    }


    @OnClick(R.id.btn2)
    public void onClick2(View view){
        startActivity(getCallingIntent(this,WebViewActivity.class));
    }

    @OnClick(R.id.btn3)
    public void onClick3(View view){
        startActivity(getCallingIntent(this,EventBusActivity.class));
    }

    @OnClick(R.id.btn4)
    public void onClick4(View view){
        startActivity(getCallingIntent(this,XmlParserActivity.class));
    }

    @OnClick(R.id.btn5)
    public void onClick5(View view){
        startActivity(getCallingIntent(this,NotifiListActivity.class));
    }


}
