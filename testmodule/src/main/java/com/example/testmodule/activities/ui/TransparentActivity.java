package com.example.testmodule.activities.ui;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.testmodule.BaseAcitivity;
import com.example.testmodule.R;

import butterknife.ButterKnife;

public class TransparentActivity extends BaseAcitivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        WindowManager.LayoutParams mLayoutParams = this.getWindow().getAttributes();
        mLayoutParams.dimAmount = 1.0f;//设置灰度,0.0f完全不暗，1.0f全暗
//        mLayoutParams.alpha = 0.5f;//1.0完全不透明，0.0f完全透明
        this.getWindow().setAttributes(mLayoutParams);
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);//提示AndroidRuntime: java.lang.IllegalArgumentException: Window type can not be changed after the window is added.
    }
}
