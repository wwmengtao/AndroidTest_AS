package com.example.testmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventBusActivity extends BaseAcitivity {
    private Unbinder mUnbinder;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, EventBusActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mUnbinder.unbind();
    }
}
