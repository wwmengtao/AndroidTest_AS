package com.example.testmodule;

import android.os.Bundle;

import butterknife.ButterKnife;

public class EventBusActivity extends BaseAcitivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        mUnbinder = ButterKnife.bind(this);
    }

}
