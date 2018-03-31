package com.example.testmodule.activities.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.example.testmodule.ALog;
import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;
import com.example.testmodule.windowmanager.FloatingMeasureView;
import com.example.testmodule.windowmanager.FloatingStatusBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WindowManagerActivity extends BaseActivity implements FloatingMeasureView.ConfigurationChangedListener,
        FloatingMeasureView.MeasureViewHeightChangedListener{

    private Context mContext = null;
    private WindowManager mWindowManager = null;
    private FloatingMeasureView mFloatingMeasureView = null;
    private FloatingStatusBarView mFloatingStatusBarView= null;

    @BindView(R.id.btn1)Button btn1;
    @BindView(R.id.btn2)Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_manager);
        init(this);
        mUnbinder = ButterKnife.bind(this);
    }

    private void init(Context context){
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        //1、使用FloatingMeasureView监听窗口状态变化
        mFloatingMeasureView = new FloatingMeasureView(mContext);
        mFloatingMeasureView.registerMeasureViewHeightChangedListener(this);
        mFloatingMeasureView.registerConfigurationChangedListener(this);
        mWindowManager.addView(mFloatingMeasureView, mFloatingMeasureView.getLayoutParams());
//        mFloatingMeasureView.post(new Runnable() {
//            @Override
//            public void run() {
//                mWindowManager.addView(mFloatingMeasureView, mFloatingMeasureView.getLayoutParams());
//            }
//        });
        //2、初始化状态栏背景色悬浮视图
        this.mFloatingStatusBarView = new FloatingStatusBarView(mContext);
    }

    @OnClick(R.id.btn1)
    public void onClick(){
        mFloatingStatusBarView.updateView(-1);
//        mFloatingStatusBarView.post(new Runnable() {
//            @Override
//            public void run() {
//                mFloatingStatusBarView.updateView(-1);
//            }
//        });

    }

    @OnClick(R.id.btn2)
    public void onClick2(){
        mFloatingStatusBarView.removeView();
    }

    @Override
    public void onDestroy() {
        mWindowManager.removeView(mFloatingMeasureView);
        mFloatingStatusBarView.removeView();
        super.onDestroy();
    }

    @Override
    public void onMeasureViewHeightChanged(boolean heightChanged, boolean heightBecomeLarger) {
        ALog.Log1(TAG+"_onMeasureViewHeightChanged heightChanged: "+heightChanged+" heightBecomeLarger: "+heightBecomeLarger);
        if(heightBecomeLarger){
            mFloatingStatusBarView.removeView();
        }else{
            mFloatingStatusBarView.updateView(-1);//恢复statusBarView
        }
    }

    @Override
    public void onConfigurationChanged(boolean isPortrait) {
        ALog.Log1(TAG+"_onConfigurationChanged isPortrait: "+isPortrait);
        mFloatingStatusBarView.setFSLayoutParams();//转屏后需要重新设置statusBarView的LayoutParams参数
        mFloatingStatusBarView.updateView(isPortrait ? android.R.color.holo_purple : android.R.color.holo_green_dark);
    }
}
